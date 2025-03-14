package com.management.demo.order;

import com.management.demo.item.Item;
import com.management.demo.stockmovement.StockMovement;
import com.management.demo.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @CreatedDate
    private LocalDateTime createdDate;

    @ManyToOne
    private Item item;

    private int quantity;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    // List of StockMovements that helped complete the order
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private List<StockMovement> stockMovements = new ArrayList<>();

    public Order(Item item, int quantity, User user) {
        this.item = item;
        this.quantity = quantity;
        this.user = user;
    }

    public int getTotalStockMovements() {
        return stockMovements.stream().mapToInt(StockMovement::getQuantity).sum();
    }

    public void completeOrder() {
        this.status = OrderStatus.COMPLETED;
    }

    // Associates a StockMovement with the order without modifying the StockMovement in the database
    public void addStockMovement(StockMovement stockMovement) {
        this.stockMovements.add(stockMovement);
    }
}
