package com.management.demo.stockmovement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;
import java.util.UUID;

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
    public StockMovementDTO createStockMovement(@RequestBody StockMovementDTO stockMovementDTO) throws MessagingException {
        return stockMovementService.createStockMovement(stockMovementDTO);
    }

    @GetMapping
    public List<StockMovementDTO> getAllStockMovements() {
        return stockMovementService.getAllStockMovements();
    }

    @GetMapping("/{id}")
    public StockMovementDTO getStockMovementById(@PathVariable UUID id) {
        return stockMovementService.getStockMovementById(id);
    }

}

