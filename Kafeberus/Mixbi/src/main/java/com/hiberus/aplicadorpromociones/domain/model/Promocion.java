package com.hiberus.aplicadorpromociones.domain.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Promocion {

    @Id
    String nombre;
    double descuento;

    @OneToMany( mappedBy = "promocion", cascade = CascadeType.ALL )
    List<PrendaPromocionada> prendasPromocionadas;

    public Promocion(String nombre, double descuento) {
        this.nombre = nombre;
        this.descuento = descuento;
    }
}
