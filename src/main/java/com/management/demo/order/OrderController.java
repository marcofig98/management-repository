package com.management.demo.order;

import com.management.demo.stockmovement.StockMovement;
import com.management.demo.stockmovement.StockMovementDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping
    @Operation(summary = "Create order", description = "You should provide only userId and ItemId in the body and ignore the other fields ")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO createOrder(@RequestBody OrderDTO orderDTO) throws MessagingException {
        return orderService.createOrder(orderDTO);
    }

    @GetMapping
    @Operation(summary = "List all orders", description = "This endpoint returns a list of all orders.")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDTO> getAllOrders(@RequestParam(value = "status", required = false) OrderStatus status) {
        return orderService.getAllOrders(status);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "This endpoint returns a single order by its ID.")
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO getOrderById(@PathVariable("id") UUID id) {
        return orderService.getOrderById(id);
    }

    @GetMapping("/{id}/stock-movements")
    @Operation(summary = "Get Stock-movements by Order ID", description = "This endpoint returns a List of stock-movements by order ID.")
    @ResponseStatus(HttpStatus.OK)
    public List<StockMovementDTO>getStockMovementsByOrderId(@PathVariable("id") UUID id) {
        return orderService.getStockMovementsByOrderId(id);
    }
}

