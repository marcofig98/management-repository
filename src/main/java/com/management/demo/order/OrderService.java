package com.management.demo.order;

import com.management.demo.email.EmailService;
import com.management.demo.item.IItemRepository;
import com.management.demo.item.Item;
import com.management.demo.item.ItemDTO;
import com.management.demo.item.exception.ItemNotFoundException;
import com.management.demo.order.exception.OrderNotFoundException;
import com.management.demo.stockmovement.IStockMovementRepository;
import com.management.demo.stockmovement.StockMovement;
import com.management.demo.stockmovement.StockMovementDTO;
import com.management.demo.user.IUserRepository;
import com.management.demo.user.User;
import com.management.demo.user.exceptions.UserNotFoundException;
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

    @Transactional
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
        Order order = new Order(item, orderDTO.getQuantity(), user);
        if (availableStock >= orderDTO.getQuantity()) {

            order.setStatus(OrderStatus.PENDING);
            orderRepository.save(order);

            log.info("Order created, id: {}, item: {}, quantity: {}, status: {}",
                    order.getId(), order.getItem().getName(),order.getQuantity(), order.getStatus().name());

            // create stock movement
            StockMovement stockMovement = new StockMovement(item, -orderDTO.getQuantity());
            stockMovementRepository.save(stockMovement);
            log.info("Stock-movement created with id: {}, for order with id: {}, quantity: {}",
                    stockMovement.getId(), order.getId(), stockMovement.getQuantity());

            order.addStockMovement(stockMovement);
            order.setStatus(OrderStatus.COMPLETED);
            orderRepository.save(order);

            sendOrderConfirmationEmail(order);
            log.info("ORDER COMPLETED, id: {}, item: {}, status: {}",
                    order.getId(), order.getItem().getName(), order.getStatus().name());

        } else {

            order.setStatus(OrderStatus.PENDING);
            orderRepository.save(order);
            log.info("Order created, id: {}, item: {}, quantity: {}, status: {}",
                    order.getId(), order.getItem().getName(), order.getQuantity(), order.getStatus().name());

        }
        return OrderMapper.toDTO(order);
    }

    private void sendOrderConfirmationEmail(Order order) throws MessagingException {

        String orderDetails = "Order ID: " + order.getId() + ";" +
                "\nItem: " + order.getItem().getName() + ";" +
                "\nQuantity: " + order.getQuantity() + ";" +
                "\nStatus: " + order.getStatus();

        try{
            emailService.sendOrderConfirmationEmail(order.getUser().getEmail(), orderDetails);
            log.info("Email sent to user with id: {}, email: {}, order id: {}, order status: {}",
                    order.getUser().getId(),
                    order.getUser().getEmail(),
                    order.getId(),
                    order.getStatus().name());

        } catch (Exception e){
            log.error("Error sending email to {}. Message: {}", order.getUser().getEmail(), e.getMessage());
            throw e;
        }
    }

    public List<OrderDTO> getAllOrders(OrderStatus status) {
        List<Order> orders;

        if (status != null) {
            orders = orderRepository.findByStatus(status);
        } else {
            orders = orderRepository.findAll();
        }

        List<OrderDTO> orderDTOs = new ArrayList<>();

        for (Order order : orders) {
            orderDTOs.add(OrderMapper.toDTO(order));
        }
        return orderDTOs;
    }

    public OrderDTO getOrderById(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        return OrderMapper.toDTO(order);
    }

    public List<StockMovementDTO> getStockMovementsByOrderId(UUID orderId) {

        Optional<Order> maybeOrder = orderRepository.findById(orderId);

        if(maybeOrder.isPresent()){
            Order order = maybeOrder.get();
            List<StockMovementDTO> stockMovementDTOs = new ArrayList<>();

            for (StockMovement stockMovement : order.getStockMovements()){

                StockMovementDTO stockMovementDTO = new StockMovementDTO(
                        stockMovement.getId(),
                        stockMovement.getCreationDate(),
                        new ItemDTO(stockMovement.getItem().getId(), stockMovement.getItem().getName()),
                        stockMovement.getQuantity()
                );
                stockMovementDTOs.add(stockMovementDTO);
            }
            return  stockMovementDTOs;
        }
        return new ArrayList<>();
    }
}
