package com.company.tasks.online.store.services;

import com.company.tasks.online.store.entities.Order;
import com.company.tasks.online.store.entities.Product;
import com.company.tasks.online.store.entities.user.User;
import com.company.tasks.online.store.enums.OrderStatus;
import com.company.tasks.online.store.exceptions.DBValidationException;
import com.company.tasks.online.store.exceptions.EntityNotFoundException;
import com.company.tasks.online.store.helpers.TestDataProvider;
import com.company.tasks.online.store.repositories.OrderRepository;
import com.company.tasks.online.store.repositories.ProductRepository;
import com.company.tasks.online.store.repositories.UserRepository;
import com.company.tasks.online.store.services.dto.OrderDto;
import com.company.tasks.online.store.services.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepo;

    @Mock
    private ProductRepository productRepo;

    @Mock
    private UserRepository userRepo;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderServiceImpl(orderRepo, productRepo, userRepo);
    }

    @Test
    void getAllOrders_when_ordersExist() {
        //given
        List<Order> expectedOrders = TestDataProvider.getOrders(2);

        //when
        when(orderRepo.getAllOrders()).thenReturn(expectedOrders);
        List<Order> actualOrders = orderService.getAllOrders();

        //then
        verify(orderRepo, times(1)).getAllOrders();
        assertTrue(Objects.equals(expectedOrders, actualOrders));
    }

    @Test
    void getOrderById_when_orderNotFound() {
        //when
        when(orderRepo.getOrderById(anyInt())).thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class, () -> orderService.getOrderById(anyInt()));
        verify(orderRepo, times(1)).getOrderById(anyInt());
    }

    @Test
    void getOrderById_when_orderExists() throws EntityNotFoundException {
        //given
        int orderId = 1;
        Order expectedOrder = TestDataProvider.getOrder(orderId, 1);

        //when
        when(orderRepo.getOrderById(orderId)).thenReturn(Optional.of(expectedOrder));
        Order actualOrder = orderService.getOrderById(orderId);

        //then
        assertEquals(expectedOrder, actualOrder);
        verify(orderRepo, times(1)).getOrderById(anyInt());
    }

    @Test
    void saveOrder_when_noProductFound() {
        //when
        when(productRepo.getProductsByIds(anySet())).thenReturn(null);

        //then
        assertThrows(EntityNotFoundException.class, () -> orderService.saveOrder(mock(OrderDto.class)));
        verify(productRepo, times(1)).getProductsByIds(anySet());
    }

    @Test
    void saveOrder_when_notEveryProductExist() {
        //given
        OrderDto orderDto = TestDataProvider.getOrderDto(Set.of(1, 2));

        //when
        // Products not found by provided ids
        when(productRepo.getProductsByIds(anySet())).thenReturn(new HashSet<>());

        //then
        assertThrows(EntityNotFoundException.class, (() -> orderService.saveOrder(orderDto)));
        verify(productRepo, times(1)).getProductsByIds(anySet());
    }

    @Test
    void saveOrder_when_notEveryProductHasEnoughAmount() {
        //given
        OrderDto orderDto = TestDataProvider.getOrderDto(Set.of(1, 2));

        Product product1 = TestDataProvider.getProduct(1);
        product1.setAmount(2);

        Product product2 = TestDataProvider.getProduct(2);
        //has not enough amount
        product2.setAmount(0);

        //when
        when(productRepo.getProductsByIds(anySet())).thenReturn(Set.of(product1, product2));

        //then
        assertThrows(DBValidationException.class, (() -> orderService.saveOrder(orderDto)));
        verify(productRepo, times(1)).getProductsByIds(anySet());
    }

    @Test
    void saveOrder_when_NoExceptionIsRaised() throws DBValidationException, EntityNotFoundException {
        //given
        Set<Integer> productIds = Set.of(1, 2);
        OrderDto orderDto = TestDataProvider.getOrderDto(productIds);
        Set<Product> productEntities = TestDataProvider.getProductsByIds(productIds);

        Authentication auth = mock(Authentication.class);
        SecurityContextHolder.setContext(new SecurityContextImpl(auth));

        //when
        when(productRepo.getProductsByIds(anySet())).thenReturn(productEntities);
        when(userRepo.findUserByName(anyString())).thenReturn(mock(User.class));
        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("User");

        //then
        orderService.saveOrder(orderDto);
        verify(productRepo, times(1)).getProductsByIds(anySet());
    }

    @Test
    void updateOrder_when_orderNotFound() {
        //when
        when(orderRepo.getOrderById(anyInt())).thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class, () -> orderService.updateOrder(mock(OrderDto.class)));
        verify(orderRepo, times(1)).getOrderById(anyInt());
    }

    @Test
    void updateOrder_when_orderHasNotNewStatus() {
        //given
        Order orderEntity = TestDataProvider.getOrder(1, 1);
        orderEntity.setStatus(OrderStatus.PROCESSED.getStatus());

        //when
        when(orderRepo.getOrderById(anyInt())).thenReturn(Optional.of(orderEntity));

        //then
        assertThrows(EntityNotFoundException.class, () -> orderService.updateOrder(mock(OrderDto.class)));
    }

    @Test
    void updateOrder_when_noNewProductFound() {
        //given
        int orderId = 1;
        Set<Integer> productIds = Set.of(1, 2);
        OrderDto orderDto = TestDataProvider.getOrderDto(orderId, productIds);
        Order orderEntity = TestDataProvider.getOrder(orderId, productIds);
        orderEntity.setStatus(OrderStatus.NEW.getStatus());

        //when
        when(orderRepo.getOrderById(anyInt())).thenReturn(Optional.of(orderEntity));
        when(productRepo.getProductsByIds(anySet())).thenReturn(null);

        //then
        assertThrows(DBValidationException.class, () -> orderService.updateOrder(orderDto));
    }

    @Test
    void updateOrder_when_notAllNewProductsFound() {
        //given
        int orderId = 1;
        Set<Integer> newProductIds = Set.of(1, 2);
        Set<Integer> productIdsOfOrderEntity = Set.of(1);

        //contains all info to update order
        OrderDto updateOrderDto = TestDataProvider.getOrderDto(orderId, newProductIds);

        // expect existing order from repo
        Order orderEntity = TestDataProvider.getOrder(orderId, productIdsOfOrderEntity);

        //when
        when(orderRepo.getOrderById(updateOrderDto.getId())).thenReturn(Optional.of(orderEntity));
        //simulates that corresponding product not found for all product ids
        when(productRepo.getProductsByIds(updateOrderDto.getProductIds())).thenReturn(Set.of());

        //then
        assertThrows(DBValidationException.class, () -> orderService.updateOrder(updateOrderDto));
    }

    @Test
    void updateOrder_when_amountForNewProductIsNotEnough() {
        //given
        int orderId = 1;
        Set<Integer> newProductIds = Set.of(3);
        Set<Integer> productIdsOfOrderEntity = Set.of(1, 2);

        //contains all info to update order
        OrderDto updateOrderDto = TestDataProvider.getOrderDto(orderId, newProductIds);

        // expect existing order from repo
        Order orderEntity = TestDataProvider.getOrder(orderId, productIdsOfOrderEntity);

        //expected products by new ids which do have enough amount from repo
        Set<Product> newProductEntities = TestDataProvider.getProductsByIds(newProductIds);
        newProductEntities.stream().forEach(pEntity -> pEntity.setAmount(0));

        //when
        when(orderRepo.getOrderById(updateOrderDto.getId())).thenReturn(Optional.of(orderEntity));
        when(productRepo.getProductsByIds(updateOrderDto.getProductIds())).thenReturn(newProductEntities);

        //then
        assertThrows(DBValidationException.class, () -> orderService.updateOrder(updateOrderDto));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "1,5", "5"})
    void updateOrder_when_noExceptionIsRaised(String productIdValues) throws DBValidationException, EntityNotFoundException {
        //given
        int orderId = 1;
        Set<Integer> newProductIds = Arrays.stream(productIdValues.split(",")).map(Integer::valueOf).collect(Collectors.toSet());
        Set<Integer> productIdsOfOrderEntity = Set.of(1, 2);

        //contains all info to update order
        OrderDto updateOrderDto = TestDataProvider.getOrderDto(orderId, newProductIds);

        //potential products by provided product ids
        Set<Product> newProductEntities = TestDataProvider.getProductsByIds(newProductIds);

        // expect existing order from repo
        Order orderEntity = TestDataProvider.getOrder(orderId, productIdsOfOrderEntity);

        //when
        when(orderRepo.getOrderById(updateOrderDto.getId())).thenReturn(Optional.of(orderEntity));
        when(productRepo.getProductsByIds(updateOrderDto.getProductIds())).thenReturn(newProductEntities);
        orderService.updateOrder(updateOrderDto);

        //then
        verify(productRepo, times(1)).increaseProductsAmount(anySet());
        verify(productRepo, times(1)).decreaseProductsAmount(anySet());

        Set<Integer> updatedProductIds = orderEntity.getProducts().stream().map(Product::getId).collect(Collectors.toSet());
        assertEquals(newProductIds.size(), updatedProductIds.size());
        assertTrue(updatedProductIds.containsAll(newProductIds));
    }

    @Test
    void deleteOrderById_when_orderNotFound() {
        //when
        when(orderRepo.getOrderById(anyInt())).thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class, () -> orderService.deleteOrderById(anyInt()));
        verify(orderRepo, times(1)).getOrderById(anyInt());
    }

    @Test
    void deleteOrderById_when_orderExists() throws EntityNotFoundException {
        //given
        int orderId = 1;
        Order expectedOrder = TestDataProvider.getOrder(orderId, 1);

        //when
        when(orderRepo.getOrderById(orderId)).thenReturn(Optional.of(expectedOrder));
        doNothing().when(productRepo).increaseProductsAmount(anySet());
        orderService.deleteOrderById(orderId);

        //then
        verify(orderRepo, times(1)).getOrderById(anyInt());
        verify(productRepo, times(1)).increaseProductsAmount(anySet());
        verify(orderRepo, times(1)).deleteById(anyInt());
    }

}