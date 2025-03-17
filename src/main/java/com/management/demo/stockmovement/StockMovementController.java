package com.management.demo.stockmovement;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/stock-movements")
@Tag(name = "Stock Movements", description = "Stock Movements Management controller")
public class StockMovementController {

    private final StockMovementService stockMovementService;

    @Autowired
    public StockMovementController(StockMovementService stockMovementService) {
        this.stockMovementService = stockMovementService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create Stock-movement", description = "You should provide only ItemId and quantity in the body and ignore the other fields ")
    public StockMovementDTO createStockMovement(@RequestBody StockMovementDTO stockMovementDTO) throws MessagingException {
        return stockMovementService.createStockMovement(stockMovementDTO);
    }

    @GetMapping
    @Operation(summary = "List all Stock-movement")
    public List<StockMovementDTO> getAllStockMovements() {
        return stockMovementService.getAllStockMovements();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Stock movements by id")
    public StockMovementDTO getStockMovementById(@PathVariable UUID id) {
        return stockMovementService.getStockMovementById(id);
    }
}

