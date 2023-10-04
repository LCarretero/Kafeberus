package com.hiberus.aplicadorpromociones.domain.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class Prenda {
    @Id
    private String referencia;
    private double precio;
    private double precio_promocionado;

    @ElementCollection(fetch = FetchType.EAGER,targetClass=String.class)
    private List<String> categorias;

    @OneToMany( mappedBy = "prenda", cascade = CascadeType.ALL )
    private Set<PrendaPromocionada> promociones;

    public Prenda(String referencia, double precio, double precio_promocionado, List<String> categorias) {
        this.referencia = referencia;
        this.precio = precio;
        this.precio_promocionado =  precio_promocionado;
        this.categorias = categorias;
    }

    public void aplicarPromocion(BigDecimal descuento) {
        BigDecimal precio_promocionado = new BigDecimal(this.precio_promocionado);
        if (descuento.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException();
        if (precio_promocionado.compareTo(descuento) < 0) {
            this.precio_promocionado = 0.00;
            return;
        }
        this.precio_promocionado = precio_promocionado.subtract(descuento).doubleValue();
    }

    public void desaplicarPromocion(BigDecimal descuento) {
        BigDecimal precio_promocionado = new BigDecimal(this.precio_promocionado);
        BigDecimal precio = new BigDecimal(this.precio);

        if (descuento.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException();
        if (precio.compareTo(descuento) < 0) {
            this.precio_promocionado = this.precio;
            return;
        }
        this.precio_promocionado = precio_promocionado.add(descuento).doubleValue();

    }
}
