package es.curso.kafka;

import es.curso.kafka.avro.PedidoKey;
import es.curso.kafka.avro.PedidoValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
@SpringBootApplication
@Slf4j
public class KafkaLowLevelConsumerApplication {

    public static final double IVA_FACTOR = 1.21;

    public static void main(final String[] args) {
        SpringApplication.run(KafkaLowLevelConsumerApplication.class, args);
    }

    @KafkaListener(topics = "pedidos")
    public void process(ConsumerRecord<PedidoKey, PedidoValue> pedido) {
        log.info("Received message from topic {} in partition {} and offset {} with key: {}",
            pedido.topic(), pedido.partition(), pedido.offset(), pedido.key());

        double importe = pedido.value().getImporte();
        double nuevoImporte = importe * IVA_FACTOR;

        log.info("Old price: {} | New price: {}", importe, nuevoImporte);
    }

}
