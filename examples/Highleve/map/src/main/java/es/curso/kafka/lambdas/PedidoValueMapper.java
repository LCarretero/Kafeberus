package es.curso.kafka.lambdas;

import es.curso.kafka.avro.MappedPedidoValue;
import es.curso.kafka.avro.PedidoKey;
import es.curso.kafka.avro.PedidoValue;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.springframework.stereotype.Component;

@Component
public class PedidoValueMapper implements KeyValueMapper<PedidoKey, PedidoValue, MappedPedidoValue> {
    @Override
    public MappedPedidoValue apply(PedidoKey k, PedidoValue v) {
        return MappedPedidoValue.newBuilder()
            .setDireccion(v.getDireccion())
            .setImporte(v.getImporte())
            .setTimestamp(System.currentTimeMillis())
            .build();
    }
}
