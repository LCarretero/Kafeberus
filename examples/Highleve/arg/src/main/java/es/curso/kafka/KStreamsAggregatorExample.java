package es.curso.kafka;


import es.curso.kafka.avro.PedidoUsuarioKey;
import es.curso.kafka.avro.PedidoUsuarioValue;
import es.curso.kafka.avro.ComprasUsuarioKey;
import es.curso.kafka.avro.ComprasUsuarioValue;
import es.curso.kafka.avro.EventType;
import es.curso.kafka.lambdas.Aggregator;
import es.curso.kafka.lambdas.Initializer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;


@SpringBootApplication
@Slf4j
public class KStreamsAggregatorExample {

    public static void main(final String[] args) {
        SpringApplication.run(KStreamsAggregatorExample.class, args);
    }

    @Autowired
    Aggregator aggregator;

    @Autowired
    Initializer initializer;

    @Bean
    public Supplier<Message<PedidoUsuarioValue>> supplier() {
        return () -> {
            val listaUsuarios = new ArrayList<>(List.of("Raúl", "Sergio"));
            val listaProductos = new ArrayList<>(List.of("iPhone 14 PRO MAX", "PS5", "Xbox Series X", "Solán de Cabras"));

            val usuarioRandom = listaUsuarios.get(new Random().nextInt(listaUsuarios.size()));
            val productoRandom = listaProductos.get(new Random().nextInt(listaUsuarios.size()));

            PedidoUsuarioKey pedidoUsuarioKey = PedidoUsuarioKey.newBuilder()
                .setIdUsuario(usuarioRandom)
                .setIdProducto(productoRandom)
                .build();

            PedidoUsuarioValue pedidoUsuarioValue = PedidoUsuarioValue.newBuilder()
                .setEventType(new Random().nextInt() % 2 == 0 ? EventType.CREATE : EventType.DELETE)
                .setIdProducto(productoRandom)
                .build();

            log.info("[supplier] Sending message with key: {} and value: {}", pedidoUsuarioKey, pedidoUsuarioValue);

            return MessageBuilder.withPayload(pedidoUsuarioValue).setHeader(KafkaHeaders.MESSAGE_KEY, pedidoUsuarioKey).build();
        };
    }

    @Bean
    public Function<KStream<PedidoUsuarioKey, PedidoUsuarioValue>, KStream<ComprasUsuarioKey, ComprasUsuarioValue>> aggregateMessages() {
        return comprasStream -> comprasStream
            .peek((k, v) -> log.info("[aggregateMessages] Received message with key: {} and value {}", k, v))
            .selectKey((k, v) -> ComprasUsuarioKey.newBuilder().setIdUsuario(k.getIdUsuario()).build())
            .groupByKey()
            .aggregate(initializer, aggregator, Named.as("COMPRAS_POR_USUARIO"), Materialized.as("COMPRAS_POR_USUARIO"))
            .toStream()
            .peek((k, v) -> log.info("[aggregateMessages] Sending message with key: {} and value {}", k, v));
    }
}
