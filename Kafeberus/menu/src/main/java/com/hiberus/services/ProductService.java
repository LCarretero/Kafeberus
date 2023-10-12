package com.hiberus.services;

import com.hiberus.avro.CRUDKey;
import com.hiberus.avro.OfferCRUDValue;
import com.hiberus.avro.ProductCRUDValue;
import com.hiberus.dto.ProductDTO;
import com.hiberus.exceptions.CrudBadVerbException;
import com.hiberus.exceptions.ProductNotFoundException;
import com.hiberus.mappers.ProductMapper;
import com.hiberus.models.Product;
import com.hiberus.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ProductService {

    @Autowired
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductDTO getProduct(String name) throws ProductNotFoundException {
        Optional<Product> productFromDB = productRepository.findByName(name);

        if (productFromDB.isEmpty()) {
            throw new ProductNotFoundException();
        }

        return ProductMapper.INSTANCE.mapToDTO(productFromDB.get());
    }


    //region PRIVATE_METHODS
    @KafkaListener(topics = "ticket-created")
    private void productConsumer(ConsumerRecord<CRUDKey, ProductCRUDValue> crudProduct){

    }

    @KafkaListener(topics = "crud-product")
    private void productConsumer(ConsumerRecord<CRUDKey, ProductCRUDValue> crudProduct) throws CrudBadVerbException {

        String verb = crudProduct.value().getVerb();
        Product product = ProductMapper.INSTANCE.mapToModel(crudProduct.value());
        switch (verb) {
            case "POST" -> createProduct(product);
            case "PUT" -> updateProduct(0, product);
            case "DELETE" -> deleteProduct(product.getName());
            default -> throw new CrudBadVerbException();
        }
    }

    @KafkaListener(topics = "crud-offer")
    private void offerConsumer(ConsumerRecord<CRUDKey, OfferCRUDValue> crudOffer) throws CrudBadVerbException {
        String productName = crudOffer.key().getId();
        String verb = crudOffer.value().getVerb();
        Product product = new Product();
        product.setName(productName);
        switch (verb) {
            case "POST", "PUT" -> updateProduct(crudOffer.value().getDiscount(), product);
            case "DELETE" -> updateProduct(0, product);
            default -> throw new CrudBadVerbException();
        }
    }

    private void createProduct(Product product) {
        Optional<Product> productFromDB = productRepository.findByName(product.getName());
        if (productFromDB.isPresent()) {
            log.info("Product {},already exist", productFromDB.get());
            return;
        }
        Product productForDb = Product.builder()
                .name(product.getName())
                .price(product.getPrice())
                .discountedPrice(product.getDiscountedPrice())
                .build();
        saveProduct(productForDb);
    }

    private void updateProduct(float discount, Product product) {
        Optional<Product> productFromDB = findByName(product.getName());
        if (productFromDB.isEmpty()) {
            errorMessage(product.getName());
            return;
        }
        float newPrice = discount == 0 ? product.getPrice() : calculatePrice(discount, product.getPrice());
        Product productToDB = Product.builder().id(productFromDB.get().getId())
                .name(product.getName())
                .price(product.getPrice())
                .discountedPrice(newPrice)
                .build();
        saveProduct(productToDB);
    }

    private void deleteProduct(String name) {
        Optional<Product> productFromDB = findByName(name);
        if (productFromDB.isEmpty()) {
            errorMessage(name);
            return;
        }
        productRepository.deleteById(productFromDB.get().getId());
    }

    private void errorMessage(String name) {
        log.info("Product with name{},not found", name);
    }

    private Optional<Product> findByName(String name) {
        return productRepository.findByName(name);
    }

    private void saveProduct(Product product) {
        productRepository.save(product);
    }

    private float calculatePrice(float discount, float price) {
        return price - (price * discount / 100);
    }
    //endregion
}
