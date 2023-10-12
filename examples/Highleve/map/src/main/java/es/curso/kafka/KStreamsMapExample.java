package es.curso.kafka;


import es.curso.kafka.avro.MappedPedidoKey;
import es.curso.kafka.avro.MappedPedidoValue;
import es.curso.kafka.avro.PedidoKey;
import es.curso.kafka.avro.PedidoValue;
import es.curso.kafka.lambdas.PedidoKeyMapper;
import es.curso.kafka.lambdas.PedidoValueMapper;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;


@SpringBootApplication
@Slf4j
public class KStreamsMapExample {

    public static void main(final String[] args) {
        SpringApplication.run(KStreamsMapExample.class, args);
    }

    @Bean
    public Supplier<Message<PedidoValue>> supplier() {
        return () -> {
            PedidoKey pedidoKey = PedidoKey.newBuilder()
                .setId(12345L)
                .build();
            PedidoValue pedidoValue = PedidoValue.newBuilder()
                .setProducto("iPhone 14 PRO MAX")
                .setNombre("Raúl Javierre Cabrero")
                .setImporte(1499)
                .setDireccion("Calle Inventada nº3 1ºA")
                .build();

            log.info("[supplier] Sending message with key: {}", pedidoKey);

            return MessageBuilder.withPayload(pedidoValue).setHeader(KafkaHeaders.MESSAGE_KEY, pedidoKey).build();
        };
    }

    @Bean
    public Function<KStream<PedidoKey, PedidoValue>, KStream<MappedPedidoKey, MappedPedidoValue>> mapper() {
        return pedidosStream -> pedidosStream
            .peek((k, v) -> log.info("[mapper] Received message with key: {}", k))
            .map((k, v) -> KeyValue.pair(
                MappedPedidoKey.newBuilder()
                    .setId(k.getId())
                    .setNombre(v.getNombre())
                    .setProducto(v.getProducto())
                    .build(),
                MappedPedidoValue.newBuilder()
                    .setDireccion(v.getDireccion())
                    .setImporte(v.getImporte())
                    .setTimestamp(System.currentTimeMillis())
                    .build()
            ))
            .peek((k, v) -> log.info("[mapper] Sending message with key: {}", k));
    }

    @Autowired
    PedidoKeyMapper pedidoKeyMapper;

    @Autowired
    PedidoValueMapper pedidoValueMapper;

    @Bean
    public Function<KStream<PedidoKey, PedidoValue>, KStream<MappedPedidoKey, MappedPedidoValue>> enhancedMapper() {
        return pedidosStream -> pedidosStream
            .peek((k, v) -> log.info("[enhancedMapper] Received message with key: {}", k))
            .map((k, v) -> KeyValue.pair(pedidoKeyMapper.apply(k, v), pedidoValueMapper.apply(k, v)))
            .peek((k, v) -> log.info("[enhancedMapper] Sending message with key: {}", k));
    }
}
