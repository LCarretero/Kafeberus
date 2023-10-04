package com.hiberus.aplicadorpromociones;

import com.hiberus.aplicadorpromociones.infraestructure.kafka.BinderProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication
@EnableBinding(BinderProcessor.class)
public class AplicadorPromocionesApplication {

	public static void main(String[] args) {
		SpringApplication.run(AplicadorPromocionesApplication.class, args);
	}

}
