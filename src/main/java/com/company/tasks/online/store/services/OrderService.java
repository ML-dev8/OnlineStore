package com.company.tasks.online.store.services;

import com.company.tasks.online.store.entities.Order;
import com.company.tasks.online.store.exceptions.DBValidationException;
import com.company.tasks.online.store.exceptions.EntityNotFoundException;
import com.company.tasks.online.store.services.dto.OrderDto;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public interface OrderService {
    List<Order> getAllOrders();

    Order getOrderById(int orderId) throws EntityNotFoundException;

    Order saveOrder(@NotNull OrderDto orderDto) throws DBValidationException, EntityNotFoundException;

    Order updateOrder(@NotNull OrderDto orderDto) throws DBValidationException, EntityNotFoundException;

    void deleteOrderById(int orderId) throws EntityNotFoundException;
}
