package com.hiberus.mappers;

import com.hiberus.avro.FinalTicket;
import com.hiberus.avro.TicketKey;
import com.hiberus.models.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    TicketMapper INSTANCE = Mappers.getMapper(TicketMapper.class);
    Ticket mapToModel(TicketKey key, FinalTicket value);
}
