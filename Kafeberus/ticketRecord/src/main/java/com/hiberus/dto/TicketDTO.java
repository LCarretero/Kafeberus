package com.hiberus.dto;

import java.util.Map;

public record TicketDTO(String idTable, Map<String, Integer> products, double price) {
}
