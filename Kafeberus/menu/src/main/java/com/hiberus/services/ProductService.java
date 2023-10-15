package com.hiberus.services;

import com.hiberus.avro.*;
import com.hiberus.dto.ProductDTO;
import com.hiberus.exceptions.CrudBadVerbException;
import com.hiberus.exceptions.ProductNotFoundException;
import com.hiberus.mappers.ProductMapper;
import com.hiberus.models.Product;
import com.hiberus.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class ProductService {

    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private KafkaTemplate<TableKey, ProductsInTicketValue> kafkaTemplate;

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

    public Iterable<ProductDTO> getProducts() {
        return productRepository.findAll().stream()
                .map(ProductMapper.INSTANCE::mapToDTO)
                .toList();
    }


    //region PRIVATE_METHODS
    @KafkaListener(topics = "ticket-created")
    private void productsInTicketConsumer(ConsumerRecord<TableKey, TicketValue> products) {
        ProductsInTicketValue value = ProductsInTicketValue.newBuilder()
                .setIdTicket(products.value().getIdTicket())
                .setMapOfProducts(products.value().getMapOfProducts())
                .setTotalPrice(calculatePrice(products.value().getMapOfProducts()))
                .build();

        log.info("Key{} ---- value{}",products.key(),products.value());
        kafkaTemplate.send("products-in-ticket", products.key(), value);
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
                .discountedPrice(product.getPrice())
                .build();
        saveProduct(productForDb);
    }

    private void updateProduct(float discount, Product product) {
        Optional<Product> productFromDB = findByName(product.getName());
        if (productFromDB.isEmpty()) {
            errorMessage(product.getName());
            return;
        }
        float newPrice = discount == 0 ? product.getPrice() : calculateDiscount(discount, product.getPrice());
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

    private double calculatePrice(Map<String, Integer> mapOfProducts) {
        return mapOfProducts.entrySet().stream()
                .map(entry -> {
                    String productName = entry.getKey();
                    int quantity = entry.getValue();
                    Optional<Product> product = productRepository.findByName(productName);
                    double productPrice = product.map(Product::getDiscountedPrice).orElse(5F);
                    return productPrice * quantity;
                })
                .reduce(0.0, Double::sum);
    }


    private float calculateDiscount(float discount, float price) {
        return price - (price * discount / 100);
    }
    //endregion
}
