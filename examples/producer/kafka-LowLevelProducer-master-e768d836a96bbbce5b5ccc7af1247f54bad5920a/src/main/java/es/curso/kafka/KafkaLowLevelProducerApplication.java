package es.curso.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import es.curso.kafka.avro.PedidoKey;
import es.curso.kafka.avro.PedidoValue;

@Configuration
@SpringBootApplication
public class KafkaLowLevelProducerApplication {

    @Autowired
    private KafkaTemplate<PedidoKey, PedidoValue> kafkaTemplate;

    public static void main(final String[] args) {
        SpringApplication.run(KafkaLowLevelProducerApplication.class, args);
    }

    @EventListener(ContextRefreshedEvent.class)
    public void send() {
        PedidoKey key = PedidoKey.newBuilder().setId(2184194).build();
        PedidoValue value = PedidoValue.newBuilder()
            .setNombre("Raúl Javierre Cabrero")
            .setImporte(1450.00)
            .setDireccion("Calle Inventada nº3 1ºA")
            .setProducto("iPhone 14 PRO MAX")
            .build();

        kafkaTemplate.send("pedidos", key, value);
    }

}
