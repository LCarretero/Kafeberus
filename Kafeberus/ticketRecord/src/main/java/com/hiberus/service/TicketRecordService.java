package com.hiberus.service;

import com.hiberus.avro.FinalTicket;
import com.hiberus.avro.TicketKey;
import com.hiberus.models.Ticket;
import com.hiberus.repository.TicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.function.Consumer;


@Service
@Slf4j
public class TicketRecordService {
    @Autowired
    private TicketRepository ticketRepository;

    @Bean
    private Consumer<KStream<TicketKey, FinalTicket>> process() {
        return record -> record
                .peek((k, v) -> {
                    Ticket ticketForDb = Ticket.builder()
                            .idTicket(UUID.fromString(k.getIdTicket()))
                            .timeStamp(Instant.now().toString())
                            .price(v.getPrice())
                            .build();
                    ticketRepository.save(ticketForDb);
                    log.info("Key:{} --- value:{}", k, v);
                });
    }
}
