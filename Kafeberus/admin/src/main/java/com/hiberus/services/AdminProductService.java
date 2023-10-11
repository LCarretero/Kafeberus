package com.hiberus.services;

import com.hiberus.Exception.BadRequestException;
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
public class AdminProductService {

    @Autowired
    private KafkaTemplate<CRUDKey, ProductCRUDValue> kafkaTemplate;

    @Value("${KEYPASS}")
    private String KEYPASS;

    public ProductDTO crudOperation(String Authorization, ProductDTO product, DbbVerbs verb) throws UnauthorizedException, BadRequestException {
        if (!authorized(Authorization))
            throw new UnauthorizedException();
        if (!validName(product.name()))
            throw new BadRequestException();

        ProductCRUDValue response = sendToProductTopic(verb.toString().toUpperCase(), product);
        return ProductCRUDMapper.INSTANCE.mapToDto(response);
    }

    private ProductCRUDValue sendToProductTopic(String verb, ProductDTO data) {
        CRUDKey key = CRUDKey.newBuilder().setVerb(verb).build();

        ProductCRUDValue value = ProductCRUDValue.newBuilder()
                .setName(data.name())
                .setPrice(data.price())
                .setDiscountedPrice(0)
                .build();
        kafkaTemplate.send("crud-product", key, value);
        return value;
    }

    private boolean validName(String name) {
        return name != null && !name.isEmpty() && !name.matches(".*\\d.*");
    }

    private boolean authorized(String Authorization) {
        return KEYPASS.equals(Authorization);
    }
}
