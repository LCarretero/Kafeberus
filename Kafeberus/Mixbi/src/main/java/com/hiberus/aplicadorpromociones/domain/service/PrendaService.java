package com.hiberus.aplicadorpromociones.domain.service;

import com.hiberus.aplicadorpromociones.domain.model.Prenda;

import java.util.List;

public interface PrendaService {
    void crear(Prenda prenda);
    void crear(List<Prenda> prenda);
    void eliminar(String referencia);
}
