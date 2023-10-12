package com.hiberus.models;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Table(name = "users")
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private UUID id;
    private String name;
    @Setter
    private int fidelity;
}
