package com.hiberus.aplicadorpromociones.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrendaPromocionada {
    PrendaPromocionada(PrendaPromocionadaPkey id) {}
    @EmbeddedId
    PrendaPromocionadaPkey id;

    @ManyToOne()
    @JoinColumn(name = "promocion_de_prenda_promocion")
    @MapsId("promocion")
    Promocion promocion;

    @ManyToOne()
    @JoinColumn(name = "promocion_de_prenda_prenda")
    @MapsId("prenda")
    Prenda prenda;

}
