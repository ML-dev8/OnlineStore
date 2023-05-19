package com.company.tasks.online.store.repositories;

import com.company.tasks.online.store.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query(value = "select EXISTS (select 1 from orders o " +
            "inner join orders_products op " +
            "on o.id = op.order_id " +
            "where op.product_id=:productId and o.status='N')", nativeQuery = true)
    boolean isAssignedToAnyOrder(@Param("productId") int productId);

    @Modifying
    @Query("update Product p " +
            "set p.amount = p.amount + 1 " +
            "where p.id in :productIds")
    void increaseProductsAmount(@Param("productIds") Set<Integer> productIds);

    @Modifying
    @Query("update Product p " +
            "set p.amount = p.amount - 1 " +
            "where p.id in :productIds")
    void decreaseProductsAmount(@Param("productIds") Set<Integer> productIds);

    @Query("select p from Product p where p.id in :productIds")
    Set<Product> getProductsByIds(@Param("productIds") Set<Integer> productIds);
}
