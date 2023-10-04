package com.hiberus.aplicadorpromociones.application;

import com.hiberus.aplicadorpromociones.domain.exceptions.PrendaNoExiste;
import com.hiberus.aplicadorpromociones.domain.exceptions.PromocionNoExiste;
import com.hiberus.aplicadorpromociones.domain.model.Prenda;
import com.hiberus.aplicadorpromociones.domain.model.PrendaPromocionada;
import com.hiberus.aplicadorpromociones.domain.model.PrendaPromocionadaPkey;
import com.hiberus.aplicadorpromociones.domain.model.Promocion;
import com.hiberus.aplicadorpromociones.domain.repository.PrendaPromocionadaRepository;
import com.hiberus.aplicadorpromociones.domain.repository.PrendaRepository;
import com.hiberus.aplicadorpromociones.domain.repository.PromocionRepository;
import com.hiberus.aplicadorpromociones.domain.service.PrendaService;

import java.math.BigDecimal;


public final class AplicarPromocion {
    private AplicarPromocion(){}

    public static void aplicar(
            PrendaPromocionadaPkey prendaPromocionadaPkey,
            PrendaRepository prendaRepository,
            PromocionRepository promocionRepository,
            PrendaPromocionadaRepository prendaPromocionadaRepository,
            PrendaService prendaService
    ) throws PrendaNoExiste, PromocionNoExiste {
        Prenda prenda = prendaRepository.findById(prendaPromocionadaPkey.getPrenda()).orElseThrow(PrendaNoExiste::new);
        Promocion promocion = promocionRepository.findById(prendaPromocionadaPkey.getPromocion()).orElseThrow(PromocionNoExiste::new);

        if(!prendaPromocionadaRepository.existsById(prendaPromocionadaPkey)) {
            PrendaPromocionada prendaPromocionada = new PrendaPromocionada(prendaPromocionadaPkey, promocion, prenda);
            prendaPromocionadaRepository.save(prendaPromocionada);

            prenda.aplicarPromocion(BigDecimal.valueOf(promocion.getDescuento()));
            prendaRepository.save(prenda);
            prendaService.crear(prenda);
        }

    }
}
