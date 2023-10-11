package com.hiberus.mapper;

import com.hiberus.avro.OrderValue;
import com.hiberus.dto.OrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", imports = java.time.Instant.class)
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderDTO mapToDTO(OrderValue orderValue);

    OrderValue mapToModel(OrderDTO orderDTO);
}
