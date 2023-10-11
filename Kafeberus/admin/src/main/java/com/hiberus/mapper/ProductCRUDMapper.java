package com.hiberus.mapper;

import com.hiberus.avro.ProductCRUDValue;
import com.hiberus.dto.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductCRUDMapper {
    ProductCRUDMapper INSTANCE = Mappers.getMapper(ProductCRUDMapper.class);

    ProductDTO mapToDto(ProductCRUDValue product);
}
