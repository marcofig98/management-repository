package com.management.demo.order;

import com.management.demo.item.IItemRepository;
import com.management.demo.item.Item;
import com.management.demo.item.ItemDTO;
import com.management.demo.item.exception.ItemNotFoundException;
import com.management.demo.order.exception.OrderNotFoundException;
import com.management.demo.stockmovement.IStockMovementRepository;
import com.management.demo.stockmovement.StockMovement;
import com.management.demo.stockmovement.StockMovementDTO;
import com.management.demo.user.User;
import com.management.demo.user.IUserRepository;
import com.management.demo.user.exceptions.UserNotFoundException;
import com.management.demo.email.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

            // create stock movement
            StockMovement stockMovement = new StockMovement(item, -orderDTO.getQuantity());
            stockMovementRepository.save(stockMovement);

            order.addStockMovement(stockMovement);
            orderRepository.save(order);

            sendOrderConfirmationEmail(order);

            return OrderMapper.toDTO(order);
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
