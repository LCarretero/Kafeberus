package com.hiberus.aplicadorpromociones.domain.repository;

import com.hiberus.aplicadorpromociones.domain.model.PrendaPromocionada;
import com.hiberus.aplicadorpromociones.domain.model.PrendaPromocionadaPkey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrendaPromocionadaRepository extends JpaRepository<PrendaPromocionada, PrendaPromocionadaPkey> {
    List<PrendaPromocionada> findByIdPromocion(String promocion);
}
