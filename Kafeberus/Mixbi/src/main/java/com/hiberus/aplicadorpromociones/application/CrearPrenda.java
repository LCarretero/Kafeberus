package com.hiberus.aplicadorpromociones.application;

import com.hiberus.aplicadorpromociones.domain.model.Prenda;
import com.hiberus.aplicadorpromociones.domain.repository.PrendaRepository;
import com.hiberus.aplicadorpromociones.domain.service.PrendaService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class CrearPrenda {
    private CrearPrenda(){}
    public static void crear(Prenda prenda, PrendaRepository prendaRepository, PrendaService prendaService) {
        if(prenda == null) {
            log.error("Prenda no puede ser nula");
            return;
        }

        log.debug("Crear prenda {}", prenda.getReferencia());
        prendaRepository.save(prenda);
        prendaService.crear(prenda);
    }
}
