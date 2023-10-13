package com.hiberus.configuration;

import com.hiberus.repositories.ProductRepository;
import com.hiberus.services.ProductService;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {
    @Bean
    public ProductService productService(ProductRepository productRepository) {
        return new ProductService(productRepository);
    }
}
