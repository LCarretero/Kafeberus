package com.hiberus.configuration;

import com.hiberus.models.Product;
import com.hiberus.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.UUID;

@Configuration
public class ConfigData {
    @Bean("ConfigData")
    CommandLineRunner commandLineRunner(ProductRepository productRepository) {
        return args -> {
            Product product1 = Product.builder()
                    .name("Café con leche")
                    .id(UUID.randomUUID())
                    .discountedPrice(1.1F)
                    .price(1.1F)
                    .build();

            Product product2 = Product.builder()
                    .name("Pulevita")
                    .id(UUID.randomUUID())
                    .discountedPrice(2F)
                    .price(2F)
                    .build();

            productRepository.saveAll(List.of(product1, product2));
        };
    }
}
