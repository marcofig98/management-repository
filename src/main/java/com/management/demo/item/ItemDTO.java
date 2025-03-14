package com.management.demo.item;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ItemDTO {

    private UUID id;
    private String name;
}
