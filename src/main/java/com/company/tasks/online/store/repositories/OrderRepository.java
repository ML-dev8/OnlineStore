package com.company.tasks.online.store.repositories;

import com.company.tasks.online.store.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("select distinct o from Order o " +
            "join fetch o.products p " +
            "join fetch o.user ")
    List<Order> getAllOrders();

    @Query("select distinct o from Order o " +
            "join fetch o.products p " +
            "join fetch o.user " +
            "where o.id = :orderId")
    Optional<Order> getOrderById(@Param("orderId") int orderId);


}
