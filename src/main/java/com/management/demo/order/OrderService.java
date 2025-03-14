package com.management.demo.order;

import com.management.demo.item.IItemRepository;
import com.management.demo.item.Item;
import com.management.demo.item.exception.ItemNotFoundException;
import com.management.demo.stockmovement.IStockMovementRepository;
import com.management.demo.stockmovement.StockMovement;
import com.management.demo.user.User;
import com.management.demo.user.IUserRepository;
import com.management.demo.user.exceptions.UserNotFoundException;
import com.management.demo.email.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Optional;

@Service
@Slf4j
public class OrderService {

    private final IItemRepository itemRepository;
    private final IStockMovementRepository stockMovementRepository;
    private final IOrderRepository orderRepository;
    private final IUserRepository userRepository;
    private final EmailService emailService;

    @Autowired
    public OrderService(IItemRepository itemRepository,
                        IStockMovementRepository stockMovementRepository,
                        IOrderRepository orderRepository,
                        IUserRepository userRepository,
                        EmailService emailService) {
        this.itemRepository = itemRepository;
        this.stockMovementRepository = stockMovementRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public OrderDTO createOrder(OrderDTO orderDTO) throws MessagingException {

        Optional<Item> itemOpt = itemRepository.findById(orderDTO.getItem().getId());
        if (!itemOpt.isPresent()) {
            throw new ItemNotFoundException(orderDTO.getItem().getId());
        }
        Item item = itemOpt.get();

        Optional<User> userOpt = userRepository.findById(orderDTO.getUser().getId());
        if (!userOpt.isPresent()) {
            throw new UserNotFoundException(orderDTO.getUser().getId());
        }
        User user = userOpt.get();

        int availableStock = itemRepository.getAvailableStock(item.getId());

        // check if there is available stock
        if (availableStock >= orderDTO.getQuantity()) {

            Order order = new Order(item, orderDTO.getQuantity(), user);
            order.setStatus(OrderStatus.COMPLETED);
            orderRepository.save(order);

            // create stock movement
            StockMovement stockMovement = new StockMovement(item, -orderDTO.getQuantity());
            stockMovementRepository.save(stockMovement);

            sendOrderConfirmationEmail(order);

            return OrderMapper.toDTO(order);  // Retornar o DTO da ordem criada
        } else {

            Order order = new Order(item, orderDTO.getQuantity(), user);
            order.setStatus(OrderStatus.PENDING);
            orderRepository.save(order);

            return OrderMapper.toDTO(order);
        }
    }

    private void sendOrderConfirmationEmail(Order order) throws MessagingException {

        String orderDetails = "Order ID: " + order.getId() +
                "\nItem: " + order.getItem().getName() +
                "\nQuantity: " + order.getQuantity() +
                "\nStatus: " + order.getStatus();

        emailService.sendOrderConfirmationEmail(order.getUser().getEmail(), orderDetails);
        log.info("ORDER COMPLETED " + order.getId());
    }

}
