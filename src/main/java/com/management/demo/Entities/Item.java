package com.management.demo.Entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    @OneToMany(mappedBy = "item")
    private List<StockMovement> stockMovements;

    @OneToMany(mappedBy = "item")
    private List<Order> orders;

    public Item(String name, List<StockMovement> stockMovements, List<Order> orders) {
        this.name = name;
        this.stockMovements = stockMovements;
        this.orders = orders;
    }
}
