package com.hiberus.mappers;

import com.hiberus.dto.ProductDTO;
import com.hiberus.models.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDTO mapToDTO(Product product);
}
