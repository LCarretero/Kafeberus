package com.hiberus.models;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Table(name = "products")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Setter
    private String name;
    private float price;
    private float discountedPrice;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + (id == null ? "null" : id) +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
