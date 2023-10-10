package com.hiberus.mapper;

import com.hiberus.avro.ProductCRUDValue;
import com.hiberus.dto.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductCRUDMapper {
    public ProductCRUDMapper INSTANCE = Mappers.getMapper(ProductCRUDMapper.class);

    public ProductDTO mapToDto(ProductCRUDValue product);
}
