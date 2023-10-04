package com.hiberus.aplicadorpromociones.application;

import com.hiberus.aplicadorpromociones.domain.model.Promocion;
import com.hiberus.aplicadorpromociones.domain.repository.PromocionRepository;

public final class CrearPromocion {
    private CrearPromocion(){}
    public static void crear(Promocion promocion, PromocionRepository promocionRepository) {
        promocionRepository.save(promocion);
    }
}
