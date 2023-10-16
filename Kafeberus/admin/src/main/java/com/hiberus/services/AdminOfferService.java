package com.hiberus.services;

import com.hiberus.exception.BadRequestException;
import com.hiberus.exception.UnauthorizedException;
import com.hiberus.avro.CRUDKey;
import com.hiberus.avro.OfferCRUDValue;
import com.hiberus.dto.OfferDTO;
import com.hiberus.enums.DbbVerbs;
import com.hiberus.mapper.OfferCRUDMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AdminOfferService {
    @Autowired
    private KafkaTemplate<CRUDKey, OfferCRUDValue> kafkaTemplate;
    @Value("${KEYPASS}")
    private String keyPass;

    public OfferDTO crudOperation(String authorization, OfferDTO offer, DbbVerbs verb) throws UnauthorizedException, BadRequestException {
        if (!authorized(authorization))
            throw new UnauthorizedException();
        if (!validName(offer.productName()))
            throw new BadRequestException();
        return OfferCRUDMapper.INSTANCE.mapToDTO(sendToTopic(verb.toString(), offer));
    }

    private OfferCRUDValue sendToTopic(String verb, OfferDTO data) {
        CRUDKey key = CRUDKey.newBuilder().setId(data.productName()).build();
        OfferCRUDValue value = OfferCRUDValue.newBuilder()
                .setProductName(data.productName())
                .setDiscount(data.discount())
                .setVerb(verb)
                .build();
        kafkaTemplate.send("crud-offer", key, value);
        return value;
    }

    private boolean validName(String name) {
        return name != null && !name.isEmpty() &&!name.matches("\\D{1,100}");
    }

    private boolean authorized(String authorization) {
        return keyPass.equals(authorization);
    }
}
