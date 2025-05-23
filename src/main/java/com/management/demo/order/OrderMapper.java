package com.management.demo.order;

import com.management.demo.item.ItemMapper;
import com.management.demo.stockmovement.StockMovementMapper;
import com.management.demo.user.UserMapper;

public class OrderMapper {

    public static OrderDTO toDTO(Order order) {
        return new OrderDTO(
                order.getId(),
                order.getCreatedDate(),
                ItemMapper.toDTO(order.getItem()),
                order.getQuantity(),
                order.getStatus(),
                UserMapper.toDTO(order.getUser()),
                StockMovementMapper.toDTOList(order.getStockMovements())
        );
    }

    public static Order toEntity(OrderDTO orderDTO) {
        return new Order(
                ItemMapper.toEntity(orderDTO.getItem()),
                orderDTO.getQuantity(),
                UserMapper.toEntity(orderDTO.getUser())
        );
    }
}
