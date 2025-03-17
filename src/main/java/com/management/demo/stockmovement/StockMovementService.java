package com.management.demo.stockmovement;

import com.management.demo.email.EmailService;
import com.management.demo.item.IItemRepository;
import com.management.demo.item.Item;
import com.management.demo.item.ItemDTO;
import com.management.demo.item.exception.ItemNotFoundException;
import com.management.demo.order.IOrderRepository;
import com.management.demo.order.Order;
import com.management.demo.order.OrderStatus;
import com.management.demo.stockmovement.exception.StockMovementNotFoundException;
import com.management.demo.user.IUserRepository;
import com.management.demo.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class StockMovementService {

    private final IStockMovementRepository stockMovementRepository;
    private final IItemRepository itemRepository;
    private final IOrderRepository orderRepository;
    private final EmailService emailService;
    private final IUserRepository userRepository;

    @Autowired
    public StockMovementService(IStockMovementRepository stockMovementRepository,
                                IItemRepository itemRepository,
                                IOrderRepository orderRepository,
                                EmailService emailService,
                                IUserRepository userRepository) {
        this.stockMovementRepository = stockMovementRepository;
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    @Transactional
    public StockMovementDTO createStockMovement(StockMovementDTO stockMovementDTO) throws MessagingException {
        Item item = itemRepository.findById(stockMovementDTO.getItem().getId())
                .orElseThrow(() -> new ItemNotFoundException(stockMovementDTO.getItem().getId()));

        StockMovement stockMovement = new StockMovement(item, stockMovementDTO.getQuantity());
        stockMovementRepository.save(stockMovement);
        log.info("Stock-movement created for item {}, with stock-movement id: {},  quantity: {}", item.getName(),stockMovement.getId(), stockMovement.getQuantity());

        processOrders(item);

        return new StockMovementDTO(stockMovement.getId(), stockMovement.getCreationDate(),
                new ItemDTO(item.getId(), item.getName()),
                stockMovement.getQuantity());
    }

    @Transactional
    private void processOrders(Item item) throws MessagingException {

        List<Order> pendingOrders = orderRepository.findByItemAndStatus(item, OrderStatus.PENDING);
        Integer availableStock = stockMovementRepository.getTotalQuantityByItem(item.getId());

        for (Order order : pendingOrders) {

            int totalStockMovements = order.getTotalStockMovements();
            int missingQuantity = order.getQuantity() + totalStockMovements; //+ because the numbers stock movements associated to the order are negative numbers

            if (availableStock > 0 && missingQuantity > 0) {

                int allocatedQuantity = Math.min(availableStock, missingQuantity);

                StockMovement movementForOrder = new StockMovement(item, -allocatedQuantity);
                stockMovementRepository.save(movementForOrder);
                order.addStockMovement(movementForOrder);
                log.info("Stock-movement created with id: {}, for order with id: {}, quantity: {}", movementForOrder.getId(), order.getId(), movementForOrder.getQuantity());

                // if there is still available stock
                if (availableStock >= missingQuantity) {
                    order.completeOrder();

                    if (!sendOrderConfirmationEmail(order, item)){
                        log.info("ORDER COMPLETED but email not sent, orderId:" + order.getId());
                    }
                }
                availableStock -= allocatedQuantity;
            }
        }
        orderRepository.saveAll(pendingOrders);
    }

    private boolean sendOrderConfirmationEmail(Order order, Item item) throws MessagingException {

        String orderDetails = "Order ID: " + order.getId() + ";" +
                "\nItem: " + item.getName() + ";" +
                "\nQuantity: " + order.getQuantity() + ";" +
                "\nStatus: " + order.getStatus();

        Optional<User> maybeUser = userRepository.findById(order.getUser().getId());

        if (maybeUser.isPresent()) {
            User user = maybeUser.get();
            try{
                emailService.sendOrderConfirmationEmail(user.getEmail(), orderDetails);
                log.info("Email sent to user with id: {}, email: {}, order id: {}, order status: {}",
                        order.getUser().getId(),
                        order.getUser().getEmail(),
                        order.getId(),
                        order.getStatus().name());
                return true;

            }catch (Exception e){
                log.error("Error sending email to {}. Message: {}", order.getUser().getEmail(), e.getMessage());
                throw e;
            }

        } else {
            return false;
        }
    }

    public List<StockMovementDTO> getAllStockMovements() {
        List<StockMovement> stockMovements = stockMovementRepository.findAll();
        List<StockMovementDTO> stockMovementDTOs = new ArrayList<>();

        for (StockMovement stockMovement : stockMovements) {
            StockMovementDTO stockMovementDTO = new StockMovementDTO(
                    stockMovement.getId(),
                    stockMovement.getCreationDate(),
                    new ItemDTO(stockMovement.getItem().getId(), stockMovement.getItem().getName()),
                    stockMovement.getQuantity()
            );
            stockMovementDTOs.add(stockMovementDTO);
        }

        return stockMovementDTOs;
    }

    public StockMovementDTO getStockMovementById(UUID stockMovementId) {
        StockMovement stockMovement = stockMovementRepository.findById(stockMovementId)
                .orElseThrow(() -> new StockMovementNotFoundException(stockMovementId));

        return new StockMovementDTO(
                stockMovement.getId(),
                stockMovement.getCreationDate(),
                new ItemDTO(stockMovement.getItem().getId(), stockMovement.getItem().getName()),
                stockMovement.getQuantity()
        );
    }

}
