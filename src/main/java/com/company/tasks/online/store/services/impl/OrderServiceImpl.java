package com.company.tasks.online.store.services.impl;

import com.company.tasks.online.store.entities.Order;
import com.company.tasks.online.store.entities.Product;
import com.company.tasks.online.store.enums.OrderStatus;
import com.company.tasks.online.store.exceptions.DBValidationException;
import com.company.tasks.online.store.exceptions.EntityNotFoundException;
import com.company.tasks.online.store.repositories.OrderRepository;
import com.company.tasks.online.store.repositories.ProductRepository;
import com.company.tasks.online.store.repositories.UserRepository;
import com.company.tasks.online.store.services.OrderService;
import com.company.tasks.online.store.services.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepo;
    private final ProductRepository productRepo;

    private final UserRepository userRepo;

    public List<Order> getAllOrders() {
        return this.orderRepo.getAllOrders();
    }

    @Override
    public Order getOrderById(int orderId) throws EntityNotFoundException {
        return this.getOrderEntityById(orderId);
    }

    @Override
    @Transactional
    public Order saveOrder(@NotNull final OrderDto orderDto) throws DBValidationException, EntityNotFoundException {
        log.trace("Getting products from db by provided ids");
        Set<Product> productEntities = this.productRepo.getProductsByIds(orderDto.getProductIds());
        if (isNull(productEntities) || productEntities.size() != orderDto.getProductIds().size()) {
            throw new EntityNotFoundException("Invalid productIds provided");
        }

        log.trace("Checking remained products amount");
        for (Product productEntity : productEntities) {
            if (productEntity.getAmount() < 1)
                throw new DBValidationException("Not valid amount for productId=" + productEntity.getId());
        }

        log.trace("Taking products from remained ones");
        this.productRepo.decreaseProductsAmount(orderDto.getProductIds());

        log.trace("Saving new order in db");
        Order order = new Order();
        order.setName(orderDto.getName());
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        order.setUser(this.userRepo.findUserByName(userName));
        order.setStatus(OrderStatus.NEW.getStatus());
        order.setProducts(productEntities);
        this.orderRepo.save(order);
        return order;
    }

    @Override
    @Transactional
    public Order updateOrder(@NotNull OrderDto orderDto) throws DBValidationException, EntityNotFoundException {
        log.trace("Getting order from db");
        Order orderEntity = this.getOrderEntityById(orderDto.getId());

        log.trace("Checking if order is new");
        if (!orderEntity.getStatus().equals(OrderStatus.NEW.getStatus())) {
            throw new EntityNotFoundException("Current order not found");
        }

        log.trace("Getting products by provided productIds");
        Set<Product> newProductEntities = this.productRepo.getProductsByIds(orderDto.getProductIds());
        if (isNull(newProductEntities) || newProductEntities.size() != orderDto.getProductIds().size()) {
            throw new DBValidationException("Invalid productIds provided");
        }

        log.trace("Checking remaining amount for provided new productIds");
        Set<Integer> assignedProductIds = orderEntity.getProducts().stream().map(Product::getId).collect(Collectors.toSet());
        for (Product newProductEntity : newProductEntities) {
            if (!assignedProductIds.contains(newProductEntity.getId()) && newProductEntity.getAmount() < 1) {
                throw new DBValidationException("Not valid remaining amount for new productId=" + newProductEntity.getId());
            }
        }

        log.trace("Putting back assigned products");
        this.productRepo.increaseProductsAmount(assignedProductIds);

        log.trace("Taking new products");
        this.productRepo.decreaseProductsAmount(orderDto.getProductIds());

        log.trace("Updating order");
        orderEntity.setProducts(newProductEntities);
        orderEntity.setName(orderDto.getName());

        return orderEntity;
    }

    @Override
    @Transactional
    public void deleteOrderById(int orderId) throws EntityNotFoundException {
        log.trace("Getting order from db");
        Order orderEntity = this.getOrderEntityById(orderId);
        Set<Integer> productIds = orderEntity.getProducts().stream().map(Product::getId).collect(Collectors.toSet());

        log.trace("Putting back assigned products");
        this.productRepo.increaseProductsAmount(productIds);

        log.trace("Deleting order");
        this.orderRepo.deleteById(orderEntity.getId());
    }

    private Order getOrderEntityById(int OrderId) throws EntityNotFoundException {
        Optional<Order> orderEntity = this.orderRepo.getOrderById(OrderId);
        if (orderEntity.isEmpty()) {
            throw new EntityNotFoundException("Order not found");
        }
        return orderEntity.get();
    }

}
