package com.hiberus.aplicadorpromociones.domain.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PrendaNoExiste extends Exception{
    public PrendaNoExiste(String errorMessage) {
        super(errorMessage);
    }
}
