package com.hiberus.service;

import com.hiberus.avro.FinalTicket;
import com.hiberus.avro.TicketKey;
import com.hiberus.models.Ticket;
import com.hiberus.repository.TicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@Slf4j
public class TicketRecordService {
    @Autowired
    private TicketRepository ticketRepository;

    @KafkaListener(topics = "ticket")
    private void process(ConsumerRecord<TicketKey, FinalTicket> ticket) {
        UUID ticketUuid = UUID.fromString(ticket.key().getIdTicket());
        if (ticketRepository.findById(ticketUuid).isPresent())
            return;
        Ticket ticketForDb = Ticket.builder()
                .idTicket(ticketUuid)
                .timeStamp(ticket.value().getTimeStamp())
                .rewarded(ticket.value().getRewarded())
                .userId(UUID.fromString(ticket.value().getIdUser()))
                .price(ticket.value().getPrice())
                .build();
        ticketRepository.save(ticketForDb);
        log.info("Key:{} --- value:{}", ticketUuid, ticket.value());
    }

//    @Bean
//    private Consumer<KStream<TicketKey, FinalTicket>> process() {
//        return record -> record
//                .peek((k, v) -> {
//                    Ticket ticketForDb = Ticket.builder()
//                            .idTicket(UUID.fromString(k.getIdTicket()))
//                            .timeStamp(Instant.now().toString())
//                            .price(v.getPrice())
//                            .build();
//                    ticketRepository.save(ticketForDb);
//                    log.info("Key:{} --- value:{}", k, v);
//                });
//    }
}
