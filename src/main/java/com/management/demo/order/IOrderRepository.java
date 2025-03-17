package com.management.demo.order;

import com.management.demo.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IOrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByItemAndStatus(Item item, OrderStatus status);
    List<Order> findByStatus(OrderStatus status);
}
