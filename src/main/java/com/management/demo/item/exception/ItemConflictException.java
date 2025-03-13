package com.management.demo.item.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)  // Status 409 (Conflict)
public class ItemConflictException extends RuntimeException {

    public ItemConflictException(String itemName) {
        super("Item already exists: " + itemName);
    }
}
