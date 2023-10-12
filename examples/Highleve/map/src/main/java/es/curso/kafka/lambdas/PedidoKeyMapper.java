package es.curso.kafka.lambdas;

import es.curso.kafka.avro.MappedPedidoKey;
import es.curso.kafka.avro.PedidoKey;
import es.curso.kafka.avro.PedidoValue;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.springframework.stereotype.Component;

@Component
public class PedidoKeyMapper implements KeyValueMapper<PedidoKey, PedidoValue, MappedPedidoKey> {
    @Override
    public MappedPedidoKey apply(PedidoKey k, PedidoValue v) {
        return MappedPedidoKey.newBuilder()
            .setId(k.getId())
            .setNombre(v.getNombre())
            .setProducto(v.getProducto())
            .build();
    }
}
