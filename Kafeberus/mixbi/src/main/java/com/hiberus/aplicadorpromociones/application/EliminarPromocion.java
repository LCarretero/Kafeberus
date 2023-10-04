package com.hiberus.aplicadorpromociones.application;

import com.hiberus.aplicadorpromociones.domain.model.Prenda;
import com.hiberus.aplicadorpromociones.domain.model.PrendaPromocionada;
import com.hiberus.aplicadorpromociones.domain.model.Promocion;
import com.hiberus.aplicadorpromociones.domain.repository.PrendaPromocionadaRepository;
import com.hiberus.aplicadorpromociones.domain.repository.PrendaRepository;
import com.hiberus.aplicadorpromociones.domain.repository.PromocionRepository;
import com.hiberus.aplicadorpromociones.domain.service.PrendaService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class EliminarPromocion {
    private EliminarPromocion(){}

    public static void eliminar(String nombre_promocion,
                                        PromocionRepository promocionRepository,
                                        PrendaRepository prendaRepository,
                                        PrendaPromocionadaRepository prendaPromocionadaRepository,
                                        PrendaService prendaService) {
        if(promocionRepository.existsById(nombre_promocion)){
            Promocion promocion = promocionRepository.findById(nombre_promocion).get();
            List<Prenda> prendas = desaplicarPromociones(promocion, prendaRepository, prendaPromocionadaRepository);
            promocionRepository.deleteById(nombre_promocion);
            prendaService.crear(prendas);
        }
    }

    private static List<Prenda> desaplicarPromociones(Promocion promocion, PrendaRepository prendaRepository,
                                                      PrendaPromocionadaRepository prendaPromocionadaRepository) {
        List<Prenda> prendas = new ArrayList<>();
        for (PrendaPromocionada prendaPromocionada : prendaPromocionadaRepository.findByIdPromocion(promocion.getNombre())) {
            Prenda prenda = prendaPromocionada.getPrenda();
            prenda.desaplicarPromocion(new BigDecimal(promocion.getDescuento()));
            prendaRepository.save(prenda);
            prendas.add(prenda);
        }
        return prendas;
    }
}
