package com.management.demo.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    private String email;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    public User(String name, String email, List<Order> orders) {
        this.name = name;
        this.email = email;
        this.orders = orders;
    }
}
