package com.hiberus.aplicadorpromociones.domain.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PromocionNoExiste extends Exception{
    public PromocionNoExiste(String errorMessage) {
        super(errorMessage);
    }
}
