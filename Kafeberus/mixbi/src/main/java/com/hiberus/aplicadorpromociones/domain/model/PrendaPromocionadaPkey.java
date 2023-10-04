package com.hiberus.aplicadorpromociones.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@AllArgsConstructor
@Embeddable
@Getter
@NoArgsConstructor
@Builder
public class PrendaPromocionadaPkey implements Serializable {

    String prenda;

    String promocion;

}
