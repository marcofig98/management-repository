package com.management.demo.Dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class ItemDTO {
    private UUID id;
    private String name;
}
