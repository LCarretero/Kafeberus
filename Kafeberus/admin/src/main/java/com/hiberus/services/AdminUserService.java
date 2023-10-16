package com.hiberus.services;

import com.hiberus.avro.CRUDKey;
import com.hiberus.avro.UserCRUDValue;
import com.hiberus.dto.UserDTO;
import com.hiberus.enums.DbbVerbs;
import com.hiberus.exception.BadRequestException;
import com.hiberus.exception.UnauthorizedException;
import com.hiberus.mapper.UserCRUDMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service

public class AdminUserService {
    @Autowired
    private KafkaTemplate<CRUDKey, UserCRUDValue> kafkaTemplate;
    @Value("${KEYPASS}")
    private String keyPass;
    private static final int MAX_POINTS = 5;

    public UserDTO crudOperation(String authorization, DbbVerbs verb, UserDTO user) throws UnauthorizedException, BadRequestException {
        if (!isAuthorized(authorization))
            throw new UnauthorizedException();
        if (!isValidUser(user) && !verb.equals(DbbVerbs.DELETE))
            throw new BadRequestException();

        return UserCRUDMapper.INSTANCE.mapToDto(sendToTopic(verb.toString().toUpperCase(), user));
    }

    private UserCRUDValue sendToTopic(String verb, UserDTO data) {
        CRUDKey key = CRUDKey.newBuilder().setId(data.uuid() == null ? UUID.randomUUID().toString() : data.uuid()).build();
        UserCRUDValue value = UserCRUDValue.newBuilder()
                .setName(data.name())
                .setPoints(data.points())
                .setVerb(verb).build();
        kafkaTemplate.send("crud-user", key, value);
        return value;
    }

    private boolean isValidUser(UserDTO user) {
        return validName(user.name()) && (user.points() >= 0 && MAX_POINTS <= 5);
    }

    private boolean validName(String name) {
        return name != null && !name.isEmpty() && !name.matches("\\D{1,100}");
    }

    private boolean isAuthorized(String authorization) {
        return keyPass.equals(authorization);
    }
}
