package com.management.demo.order;

import com.management.demo.stockmovement.StockMovementDTO;
import com.management.demo.user.UserDTO;
import com.management.demo.item.ItemDTO;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.ReadOnlyProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class OrderDTO {

    @ReadOnlyProperty
    @Hidden
    private UUID id;

    @ReadOnlyProperty
    @Hidden
    private LocalDateTime creationDate;
    private ItemDTO item;
    private Integer quantity;
    private UserDTO user;
    @Hidden
    private List<StockMovementDTO> stockMovements;
}
