package com.management.demo.user;

import com.management.demo.order.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    @CreatedDate
    private LocalDateTime creationDate;

    public User(String name, String email, List<Order> orders) {
        this.name = name;
        this.email = email;
        this.orders = orders;
    }
}
