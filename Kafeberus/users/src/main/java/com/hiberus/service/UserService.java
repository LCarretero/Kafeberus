package com.hiberus.service;

import com.hiberus.avro.CRUDKey;
import com.hiberus.avro.UserCRUDValue;
import com.hiberus.exception.CrudBadVerbException;
import com.hiberus.mappers.UserMapper;
import com.hiberus.models.User;
import com.hiberus.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final int PROMO = 0;

    @KafkaListener(topics = "crud-user")
    private void crudConsumer(ConsumerRecord<CRUDKey, UserCRUDValue> record) throws CrudBadVerbException {

        String verb = record.value().getVerb();
        User user = UserMapper.INSTANCE.mapToModel(record.value());
        switch (verb) {
            case "POST" -> createUser(record.key().getId(), user);
            case "PUT" -> updateUser(record.key().getId(), user);
            case "DELETE" -> deleteUser(record.key().getId());
            default -> throw new CrudBadVerbException();
        }
    }

    //region PRIVATE_METHODS
    private void createUser(String id ,User user) {
        if (!validName(user.getName())) {
            log.info("User name not valid {}", user.getName());
            return;
        }
        saveUser(id, user);
    }

    private void saveUser(String id, User user) {
        if (!validId(id)) {
            return;
        }
        UUID uuid = UUID.fromString(id);
        if (userRepository.findById(uuid).isPresent())
            uuid = UUID.randomUUID();
        User userForDb = User.builder()
                .name(user.getName())
                .id(uuid)
                .id(UUID.fromString(id))
                .fidelity(PROMO)
                .build();
        userRepository.save(userForDb);
    }

    private void updateUser(String id, User user) {
        saveUser(id, user);
    }

    private void deleteUser(String id) {
        if (!validId(id)) {
            return;
        }
        UUID uuid = UUID.fromString(id);
        if (userRepository.findById(uuid).isEmpty()) {
            log.info("User id not found {}", id);
            return;
        }
        userRepository.deleteById(uuid);
    }

    private boolean validName(String name) {
        return name != null && !name.isEmpty() && !name.matches(".*\\d.*");
    }

    private boolean validId(String id) {
        try {
            UUID.fromString(id);
            return true;
        } catch (IllegalArgumentException e) {
            log.info("User id not valid {}", id);
            return false;
        }
    }
    //endregion

}
