package com.hiberus.aplicadorpromociones.application;

import com.hiberus.aplicadorpromociones.domain.repository.PrendaRepository;
import com.hiberus.aplicadorpromociones.domain.service.PrendaService;

public final class EliminarPrenda {
    private EliminarPrenda(){}
    public static void eliminar(String referencia, PrendaRepository prendaRepository, PrendaService prendaService) {
        if (prendaRepository.existsById(referencia)) {
            prendaRepository.deleteById(referencia);
            prendaService.eliminar(referencia);
        }
    }
}
