package com.management.demo.stockmovement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;


@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class StockMovementNotFoundException extends RuntimeException {

    public StockMovementNotFoundException(UUID stockMovementID) {
        super("Stock-movement Not Found " + stockMovementID);
    }
}
