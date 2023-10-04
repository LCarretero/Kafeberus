package com.hiberus.aplicadorpromociones.domain.repository;

import com.hiberus.aplicadorpromociones.domain.model.Prenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface PrendaRepository extends JpaRepository<Prenda, String> {
}
