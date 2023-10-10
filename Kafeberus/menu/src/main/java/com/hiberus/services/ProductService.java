package com.hiberus.services;

import com.hiberus.avro.CRUDKey;
import com.hiberus.avro.ProductCRUDValue;
import com.hiberus.dto.ProductDTO;
import com.hiberus.exceptions.CrudBadVerbException;
import com.hiberus.exceptions.ProductNotFoundException;
import com.hiberus.mappers.ProductMapper;
import com.hiberus.models.Product;
import com.hiberus.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static org.reflections.Reflections.log;

@Service
@AllArgsConstructor
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @KafkaListener(topics = "create-product")
    public void consumer(ConsumerRecord<CRUDKey, ProductCRUDValue> crudProduct) throws CrudBadVerbException {
        assert log != null;
        log.info("Tópic: create-product");
        log.info("key: {}", crudProduct.key());
        log.info("Headers: {}", crudProduct.headers());
        log.info("Partion: {}", crudProduct.partition());
        log.info("Order: {}", crudProduct.value());
//        String verb = crudProduct.key().getVerb();
//        Product product = ProductMapper.INSTANCE.mapAvroToModel(crudProduct.value());
//        switch (verb) {
//            case "POST":
//                Optional<Product> productFromDB = productRepository.findByName(product.getName());
//                if(productFromDB.isPresent())
//                    throw
//                ;
//            case "GET":
//                ;
//            case "PUT":
//                ;
//            case "DELETE":
//                ;
//            default:
//                throw new CrudBadVerbException();
//        }
    }

    private void createProduct(Product product) throws ProductNotFoundException {

        Optional<Product> productFromDB = productRepository.findByName(product.getName());
        if (productFromDB.isPresent())
            throw new ProductNotFoundException();

        productRepository.save(product);
    }

    public ProductDTO getProduct(UUID id) throws ProductNotFoundException {
        Optional<Product> productFromDB = productRepository.findById(id);

        if (productFromDB.isEmpty()) {
            throw new ProductNotFoundException();
        }

        return ProductMapper.INSTANCE.mapToDTO(productFromDB.get());
    }


}
