package com.management.demo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.ReadOnlyProperty;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserDTO {

    @ReadOnlyProperty
    private UUID id;
    private String name;
    private String email;
}
