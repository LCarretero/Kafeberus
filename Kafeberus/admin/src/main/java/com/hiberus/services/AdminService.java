package com.hiberus.services;

import com.hiberus.Exception.ProductBadRequestException;
import com.hiberus.Exception.UnauthorizedException;
import com.hiberus.avro.CRUDKey;
import com.hiberus.avro.ProductCRUDValue;
import com.hiberus.dto.ProductDTO;
import com.hiberus.enums.DbbVerbs;
import com.hiberus.mapper.ProductCRUDMapper;
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

    public ProductDTO crudOperation(String auth, ProductCRUDValue product, DbbVerbs verb) throws UnauthorizedException, ProductBadRequestException {
        if (!authorized(auth))
            throw new UnauthorizedException();
        if (!validProduct(product))
            throw new ProductBadRequestException();
        sendToProductTopic(product, verb.toString().toUpperCase());
        return ProductCRUDMapper.INSTANCE.mapToDto(product);
    }

    private void sendToProductTopic(ProductCRUDValue productValue, String verb) {
        CRUDKey key = CRUDKey.newBuilder().setVerb(verb).build();

        ProductCRUDValue value = ProductCRUDValue.newBuilder()
                .setName(productValue.getName())
                .setPrice(productValue.getPrice())
                .build();
        kafkaTemplate.send("crud-product", key, value);
    }

    private boolean validProduct(ProductCRUDValue productValue) {
        return validName(productValue.getName()) && productValue.getPrice() > 0;
    }

    private boolean validName(String name) {
        return name != null && !name.isEmpty() && !name.matches(".*\\d.*");
    }

    private boolean authorized(String auth) {
        return KEYPASS.equals(auth);
    }
}
