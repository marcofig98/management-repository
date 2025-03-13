package com.management.demo.stockmovements;

import com.management.demo.items.ItemDTO;
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
