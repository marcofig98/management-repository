package com.management.demo.Dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class StockMovementDTO {
    private UUID id;
    private LocalDateTime creationDate;
    private ItemDTO item;
    private Integer quantity;
}
