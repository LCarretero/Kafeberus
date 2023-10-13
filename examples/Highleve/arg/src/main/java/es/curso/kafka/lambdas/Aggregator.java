package es.curso.kafka.lambdas;

import es.curso.kafka.avro.ComprasUsuarioKey;
import es.curso.kafka.avro.ComprasUsuarioValue;
import es.curso.kafka.avro.EventType;
import es.curso.kafka.avro.PedidoUsuarioValue;
import es.curso.kafka.avro.Producto;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class Aggregator implements org.apache.kafka.streams.kstream.Aggregator<ComprasUsuarioKey, PedidoUsuarioValue, ComprasUsuarioValue> {

    @Override
    public ComprasUsuarioValue apply(ComprasUsuarioKey comprasUsuarioKey,
                                     PedidoUsuarioValue pedidoUsuarioValue,
                                     ComprasUsuarioValue comprasUsuarioValue) {

        // Lo borramos para no tener duplicados en caso de que ya exista y sea un CREATE
        comprasUsuarioValue = ComprasUsuarioValue.newBuilder()
            .setCompras(comprasUsuarioValue.getCompras()
            .stream()
            .filter(c -> !pedidoUsuarioValue.getIdProducto().equals(c.getIdProducto()))
            .collect(Collectors.toList())).build();

        if (pedidoUsuarioValue.getEventType() == EventType.CREATE) {
            comprasUsuarioValue.getCompras().add(Producto.newBuilder().setIdProducto(pedidoUsuarioValue.getIdProducto()).build());
        }

        return comprasUsuarioValue;
    }
}
