package com.hiberus.mappers;


import com.hiberus.avro.UserCRUDValue;
import com.hiberus.dto.UserDTO;
import com.hiberus.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User mapToModel(UserCRUDValue avro);
    UserDTO mapToDTO(User user);
}
