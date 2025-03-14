package com.management.demo.stockmovement;

import com.management.demo.item.IItemRepository;
import com.management.demo.item.Item;
import com.management.demo.item.ItemDTO;
import com.management.demo.item.exception.ItemNotFoundException;
import com.management.demo.order.IOrderRepository;
import com.management.demo.order.Order;
import com.management.demo.order.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class StockMovementService {

    private final IStockMovementRepository stockMovementRepository;
    private final IItemRepository itemRepository;
    private final IOrderRepository orderRepository;

    @Autowired
    public StockMovementService(IStockMovementRepository stockMovementRepository,
                                IItemRepository itemRepository,
                                IOrderRepository orderRepository) {
        this.stockMovementRepository = stockMovementRepository;
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
    }

    public StockMovementDTO createStockMovement(StockMovementDTO stockMovementDTO) {
        Item item = itemRepository.findById(stockMovementDTO.getItem().getId())
                .orElseThrow(() -> new ItemNotFoundException(stockMovementDTO.getItem().getId()));


        StockMovement stockMovement = new StockMovement(item, stockMovementDTO.getQuantity());
        stockMovementRepository.save(stockMovement);


        processOrders(item, stockMovement);

        return new StockMovementDTO(stockMovement.getId(), stockMovement.getCreationDate(),
                new ItemDTO(item.getId(), item.getName()),
                stockMovement.getQuantity());
    }

    private void processOrders(Item item, StockMovement newStockMovement) {
        List<Order> pendingOrders = orderRepository.findByItemAndStatus(item, OrderStatus.PENDING);

        Integer availableStock = stockMovementRepository.getTotalQuantityByItem(item.getId());

        for (Order order : pendingOrders) {
            int missingQuantity = order.getQuantity() - order.getTotalStockMovementsQuantity();

            if (availableStock > 0 && missingQuantity > 0) {
                int allocatedQuantity = Math.min(availableStock, missingQuantity);

                StockMovement movementForOrder = new StockMovement(item, -allocatedQuantity);
                stockMovementRepository.save(movementForOrder);

                order.addStockMovement(movementForOrder);

                if (availableStock>=missingQuantity) {
                    order.completeOrder();
                    //todo send mail to the user
                }
                availableStock -= allocatedQuantity;
            }
        }

        orderRepository.saveAll(pendingOrders);
    }

}
