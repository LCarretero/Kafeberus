package es.curso.kafka;


import es.curso.kafka.avro.CaracteristicasKey;
import es.curso.kafka.avro.CaracteristicasValue;
import es.curso.kafka.avro.PrecioKey;
import es.curso.kafka.avro.PrecioValue;
import es.curso.kafka.avro.ProductoKey;
import es.curso.kafka.avro.ProductoValue;
import es.curso.kafka.avro.Tipo;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Named;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;


@SpringBootApplication
@Slf4j
public class KStreamsJoinExample {

    public static void main(final String[] args) {
        SpringApplication.run(KStreamsJoinExample.class, args);
    }

    private final Random random = new Random();

    @Bean
    public Supplier<Message<CaracteristicasValue>> supplierCaracteristicas() {
        val listaProductos = new ArrayList<>(List.of("iPhone 14 PRO MAX", "PS5", "Xbox Series X"));
        val listaColores = new ArrayList<>(List.of("Blanco", "Azul", "Rojo", "Negro"));

        return () -> {

            val productoAleatorio = listaProductos.get(random.nextInt(listaProductos.size()));

            CaracteristicasKey caracteristicasKey = CaracteristicasKey.newBuilder()
                .setId(productoAleatorio)
                .build();
            CaracteristicasValue caracteristicasValue = CaracteristicasValue.newBuilder()
                .setColor(listaColores.get(random.nextInt(listaColores.size())))
                .setPeso(random.nextInt(10))
                .setTipo(productoAleatorio.equals("iPhone 14 PRO MAX") ? Tipo.MOVIL : Tipo.CONSOLA)
                .build();

            log.info("[supplierCaracteristicas] Sending message with key: {} and value: {}", caracteristicasKey, caracteristicasValue);

            return MessageBuilder.withPayload(caracteristicasValue).setHeader(KafkaHeaders.MESSAGE_KEY, caracteristicasKey).build();
        };
    }

    @Bean
    public Supplier<Message<PrecioValue>> supplierPrecios() {
        val productosYPrecios = new ArrayList<>(List.of(
            Pair.of("iPhone 14 PRO MAX", 1499),
            Pair.of("PS5", 550),
            Pair.of("Xbox Series X", 520)
        ));

        return () -> {

            val productoYPrecioAleatorio = productosYPrecios.get(random.nextInt(productosYPrecios.size()));

            PrecioKey precioKey = PrecioKey.newBuilder()
                .setId(productoYPrecioAleatorio.getLeft())
                .build();
            PrecioValue precioValue = PrecioValue.newBuilder()
                .setPrecio(productoYPrecioAleatorio.getRight())
                .build();

            log.info("[supplierPrecios] Sending message with key: {} and value: {}", precioKey, precioValue);

            return MessageBuilder.withPayload(precioValue).setHeader(KafkaHeaders.MESSAGE_KEY, precioKey).build();
        };
    }

    @Bean
    public BiFunction<KStream<CaracteristicasKey, CaracteristicasValue>, KStream<PrecioKey, PrecioValue>, KStream<ProductoKey, ProductoValue>> joiner() {
        return (caracteristicasStream, preciosStream) -> {
            KTable<ProductoKey, CaracteristicasValue> caracteristicasKTable = caracteristicasStream
                // Cambiamos la clave para que sean del mismo tipo y podamos hacer el join
                .selectKey((k, v) -> ProductoKey.newBuilder().setId(k.getId()).build())
                .toTable(Named.as("CARACTERISTICAS_PRODUCTOS"), Materialized.as("CARACTERISTICAS_PRODUCTOS"));

            KTable<ProductoKey, PrecioValue> preciosKTable = preciosStream
                // Cambiamos la clave para que sean del mismo tipo y podamos hacer el join
                .selectKey((k, v) -> ProductoKey.newBuilder().setId(k.getId()).build())
                .toTable(Named.as("PRECIOS_PRODUCTOS"), Materialized.as("PRECIOS_PRODUCTOS"));

            return caracteristicasKTable.join(preciosKTable,    // Las dos tablas que hacen el join

                // La función de join (se puede llevar a una clase que implemente la interfaz ValueJoiner https://kafka.apache.org/32/javadoc/org/apache/kafka/streams/kstream/ValueJoiner.html)
                (caracteristicasValue, preciosValue) -> ProductoValue.newBuilder()
                    .setColor(caracteristicasValue.getColor())
                    .setPeso(caracteristicasValue.getPeso())
                    .setTipo(caracteristicasValue.getTipo())
                    .setPrecio(preciosValue.getPrecio())
                    .build())

                // Como el resultado de un join de dos KTables es una KTable, lo volvemos a pasar a KStream
                .toStream()
                .peek((k, v) -> log.info("[joiner] Sending message with key: {} and value: {}", k, v));
        };
    }
}
