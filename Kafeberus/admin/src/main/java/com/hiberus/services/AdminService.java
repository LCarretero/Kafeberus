package com.hiberus.services;

import com.hiberus.avro.CRUDKey;
import com.hiberus.avro.ProductCRUDValue;
import com.hiberus.enums.DbbVerbs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private KafkaTemplate<CRUDKey, ProductCRUDValue> kafkaTemplate;

    @Value("${KEYPASS}")
    private String KEYPASS;

    public void createProduct(ProductCRUDValue product) {

    }

    private void sendToProductTopic(ProductCRUDValue productValue, DbbVerbs verb) {
        CRUDKey key = CRUDKey.newBuilder().setVerb(verb.toString().toUpperCase()).build();

        ProductCRUDValue value = ProductCRUDValue.newBuilder()
                .setName(productValue.getName())
                .setPrice(productValue.getPrice())
                .build();

        kafkaTemplate.send("crud-product", key, value);
    }

    private boolean validName(String name) {
        return name != null && !name.isEmpty() && !name.matches(".*\\d.*");
    }

    private boolean authorized(String auth) {
        return KEYPASS.equals(auth);
    }
}
