package com.hiberus.mapper;

import com.hiberus.avro.OfferCRUDValue;
import com.hiberus.dto.OfferDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OfferCRUDMapper {
     OfferCRUDMapper INSTANCE = Mappers.getMapper(OfferCRUDMapper.class);

    OfferDTO mapToDTO(OfferCRUDValue offerCRUDValue);
}
