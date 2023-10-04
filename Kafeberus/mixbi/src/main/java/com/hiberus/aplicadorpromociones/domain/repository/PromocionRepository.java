package com.hiberus.aplicadorpromociones.domain.repository;

import com.hiberus.aplicadorpromociones.domain.model.Promocion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromocionRepository extends JpaRepository<Promocion, String> {
}
