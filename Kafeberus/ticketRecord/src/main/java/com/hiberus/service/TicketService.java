package com.hiberus.service;

import com.hiberus.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    @Bean

}
