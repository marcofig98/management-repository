package com.management.demo.orders;

import com.management.demo.users.User;
import com.management.demo.items.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @CreatedDate
    private LocalDateTime creationDate;

    @ManyToOne
    private Item item;

    private Integer quantity;

    @ManyToOne
    private User user;

    public Order(Item item, Integer quantity, User user) {
        this.item = item;
        this.quantity = quantity;
        this.user = user;
    }
}
