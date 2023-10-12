package es.curso.kafka;

import es.curso.kafka.avro.PedidoKey;
import es.curso.kafka.avro.PedidoValue;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
@Slf4j
public class KafkaHighLevelConsumerApplication {

    public static final double IVA_FACTOR = 1.21;

    public static void main(final String[] args) {
        SpringApplication.run(KafkaHighLevelConsumerApplication.class, args);
    }

    @Bean
    public Consumer<KStream<PedidoKey, PedidoValue>> process() {
        return pedidoKeyPedidoValueKStream -> pedidoKeyPedidoValueKStream
            .peek((k, v) -> log.info("Received message with key: {}", k))
            .peek((k, v) -> {
                double importe = v.getImporte();
                double nuevoImporte = importe * IVA_FACTOR;

                log.info("Old price: {} | New price: {}", importe, nuevoImporte);
            });
    }
}
