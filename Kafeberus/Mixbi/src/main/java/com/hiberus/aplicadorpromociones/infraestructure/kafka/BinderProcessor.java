package com.hiberus.aplicadorpromociones.infraestructure.kafka;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

public interface BinderProcessor {
    String PRENDAS = "prendas";
    String PROMOCIONES = "promociones";
    String PRENDAS_PROMOCIONADAS = "prendas_promocionadas";

    @Input(PRENDAS)
    KStream<?, ?> prendas();

    @Input(PROMOCIONES)
    KStream<?, ?> promociones();

    @Output(PRENDAS_PROMOCIONADAS)
    KStream<?, ?> prendas_promocionadas();
}