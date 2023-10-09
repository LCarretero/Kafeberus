package com.hiberus.services;

import com.hiberus.avro.CRUDKey;
import com.hiberus.avro.ProductCRUDValue;
import com.hiberus.dto.ProductDTO;
import com.hiberus.exceptions.CrudBadVerbException;
import com.hiberus.exceptions.ProductNotFoundException;
import com.hiberus.mappers.ProductMapper;
import com.hiberus.models.Product;
import com.hiberus.repository.ProductRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @KafkaListener(topics = "create-product")
    public void consumer(ConsumerRecord<CRUDKey, ProductCRUDValue> crudProduct) throws CrudBadVerbException {
        String verb = crudProduct.key().getVerb();
        switch (verb) {
            case "POST":

                ;
            case "GET":
                ;
            case "PUT":
                ;
            case "DELETE":
                ;
            default:
                throw new CrudBadVerbException();
        }
    }

//    public ProductDTO createProduct(String auth) throws ProductNotFoundException, ProductBadRequestException, CrudUnauthorizedException {
//        if (!authorized(auth))
//            throw new CrudUnauthorizedException();
//
//        Optional<Product> productFromDB = productRepository.findByName(product.getName());
//        if (productFromDB.isPresent())
//            throw new ProductNotFoundException();
//        productRepository.save(ProductMapper.INSTANCE.mapAvroToModel(product));
//
//        return ProductMapper.INSTANCE.mapToDTO(product);
//    }

    public ProductDTO getProduct(UUID id) throws ProductNotFoundException {
        Optional<Product> productFromDB = productRepository.findById(id);

        if (productFromDB.isEmpty()) {
            throw new ProductNotFoundException();
        }

        return ProductMapper.INSTANCE.mapToDTO(productFromDB.get());
    }


}
