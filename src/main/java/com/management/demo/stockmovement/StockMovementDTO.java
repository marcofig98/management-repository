package com.management.demo.stockmovement;

import com.management.demo.item.ItemDTO;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class StockMovementDTO {
    private UUID id;
    @Hidden
    private LocalDateTime creationDate;
    private ItemDTO item;
    private Integer quantity;
}
