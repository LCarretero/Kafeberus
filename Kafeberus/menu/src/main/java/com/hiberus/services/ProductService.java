package com.hiberus.services;

import com.hiberus.dto.ProductDTO;
import com.hiberus.exceptions.ProductBadRequest;
import com.hiberus.exceptions.ProductNotFoundException;
import com.hiberus.mappers.ProductMapper;
import com.hiberus.models.Product;
import com.hiberus.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public ProductDTO createProduct(Product product) throws ProductNotFoundException, ProductBadRequest {
        if (!validName(product.getName()))
            throw new ProductBadRequest();
        Optional<Product> productFromDB = productRepository.findByName(product.getName());
        if (productFromDB.isPresent())
            throw new ProductNotFoundException();
        productRepository.save(product);

        return ProductMapper.INSTANCE.mapToDTO(product);
    }

    public ProductDTO getProduct(UUID id) throws ProductNotFoundException {
        Optional<Product> productFromDB = productRepository.findById(id);

        if (productFromDB.isEmpty()) {
            throw new ProductNotFoundException();
        }

        return ProductMapper.INSTANCE.mapToDTO(productFromDB.get());
    }


    private boolean validName(String name) {
        return name != null && !name.isEmpty() && !name.matches(".*\\d.*");
    }

}
