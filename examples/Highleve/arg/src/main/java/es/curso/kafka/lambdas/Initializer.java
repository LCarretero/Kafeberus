package es.curso.kafka.lambdas;

import es.curso.kafka.avro.ComprasUsuarioValue;
import java.util.ArrayList;
import org.springframework.stereotype.Component;

@Component
public class Initializer implements org.apache.kafka.streams.kstream.Initializer<ComprasUsuarioValue> {
    @Override
    public ComprasUsuarioValue apply() {
        return ComprasUsuarioValue.newBuilder()
            .setCompras(new ArrayList<>())
            .build();
    }
}
