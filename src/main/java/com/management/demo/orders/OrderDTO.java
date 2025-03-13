package com.management.demo.orders;

import com.management.demo.users.UserDTO;
import com.management.demo.items.ItemDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class OrderDTO {
    private UUID id;
    private LocalDateTime creationDate;
    private ItemDTO item;
    private Integer quantity;
    private UserDTO user;
}
