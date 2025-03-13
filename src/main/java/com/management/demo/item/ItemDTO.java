package com.management.demo.item;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.ReadOnlyProperty;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ItemDTO {

    @ReadOnlyProperty
    @Hidden
    private UUID id;
    private String name;
}
