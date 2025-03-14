package com.management.demo.stockmovement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stock-movements")
public class StockMovementController {

    private final StockMovementService stockMovementService;

    @Autowired
    public StockMovementController(StockMovementService stockMovementService) {
        this.stockMovementService = stockMovementService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StockMovementDTO createStockMovement(@RequestBody StockMovementDTO stockMovementDTO) {
        // Chama o serviço para criar o StockMovement e retornar o DTO
        return stockMovementService.createStockMovement(stockMovementDTO);
    }
}

