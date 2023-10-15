package com.hiberus.dto;

import java.util.Map;

public record TicketDTO(int idTable, Map<String, Integer> products, double price) {
}
