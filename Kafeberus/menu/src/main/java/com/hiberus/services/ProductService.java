package com.hiberus.services;

import com.hiberus.avro.CRUDKey;
import com.hiberus.avro.ProductCRUDValue;
import com.hiberus.dto.ProductDTO;
import com.hiberus.exceptions.CrudBadVerbException;
import com.hiberus.exceptions.ProductNotFoundException;
import com.hiberus.mappers.ProductMapper;
import com.hiberus.models.Product;
import com.hiberus.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ProductService {

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

    @KafkaListener(topics = "crud-product")
    public void consumer(ConsumerRecord<CRUDKey, ProductCRUDValue> crudProduct) throws CrudBadVerbException, ProductNotFoundException {

        String verb = crudProduct.key().getVerb();
        Product product = ProductMapper.INSTANCE.mapToModel(crudProduct.value());
        switch (verb) {
            case "POST" -> createProduct(product);
            case "PUT" -> updateProduct(product);
            case "DELETE" -> deleteProduct(product.getName());
            default -> throw new CrudBadVerbException();
        }
    }

    private void createProduct(Product product) {

        Optional<Product> productFromDB = productRepository.findByName(product.getName());
        if (productFromDB.isPresent()) {
            log.info("Product {},already exist", productFromDB.get());
            return;
        }

        productRepository.save(product);
    }

    private void updateProduct(Product product) {
        Optional<Product> productFromDB = productRepository.findByName(product.getName());
        if (productFromDB.isEmpty()) {
            errorMessage(product.getName());
            return;
        }

        productRepository.save(product);
    }

    private void deleteProduct(String name) {
        Optional<Product> productFromDB = productRepository.findByName(name);
        if (productFromDB.isEmpty()) {
            errorMessage(name);
            return;
        }
        productRepository.deleteByName(name);
    }

    private void errorMessage(String name) {
        log.info("Product with name{},not found", name);
    }

}
