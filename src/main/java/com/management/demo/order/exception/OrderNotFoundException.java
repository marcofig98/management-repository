package com.management.demo.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;


@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(UUID orderId) {
        super("Order Not Found " + orderId);
    }
}
