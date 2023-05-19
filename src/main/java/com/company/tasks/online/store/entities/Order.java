package com.company.tasks.online.store.entities;

import com.company.tasks.online.store.entities.user.User;
import com.company.tasks.online.store.ws.serializers.OrderProductsSerializer;
import com.company.tasks.online.store.ws.serializers.OrderUserSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "status", columnDefinition = "Char(1) CHECK (status IN ('N', 'P'))")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = OrderUserSerializer.class)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "orders_products",
            joinColumns = @JoinColumn(name = "order_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "product_id", nullable = false)
    )
    @JsonSerialize(using = OrderProductsSerializer.class)
    private Set<Product> products = new HashSet<>();

}
