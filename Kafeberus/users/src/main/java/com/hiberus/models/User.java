package com.hiberus.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Table(name = "users")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private UUID id;
    private String name;
    private byte fidelity;
}
