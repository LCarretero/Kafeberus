package com.hiberus.mapper;

import com.hiberus.avro.UserCRUDValue;
import com.hiberus.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")

public interface UserCRUDMapper {
    public UserCRUDMapper INSTANCE = Mappers.getMapper(UserCRUDMapper.class);

    public UserDTO mapToDto(UserCRUDValue product);
}
