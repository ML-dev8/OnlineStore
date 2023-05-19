package com.company.tasks.online.store.helpers;

import com.company.tasks.online.store.entities.Order;
import com.company.tasks.online.store.entities.Product;
import com.company.tasks.online.store.entities.user.Role;
import com.company.tasks.online.store.entities.user.User;
import com.company.tasks.online.store.enums.OrderStatus;
import com.company.tasks.online.store.services.dto.OrderDto;
import com.company.tasks.online.store.services.dto.ProductDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.company.tasks.online.store.enums.ROLE.ADMIN;

public class TestDataProvider {

    public static Set<Role> getRolesByNames(List<String> roleNames) {
        return IntStream
                .rangeClosed(1, roleNames.size())
                .mapToObj(index -> {
                    Role role = new Role();
                    role.setId(index);
                    role.setRole(roleNames.get(index - 1));
                    return role;
                }).collect(Collectors.toSet());
    }


    public static Product getProduct(int id) {
        Product product = new Product();
        product.setId(id);
        product.setName("productName_" + id);
        product.setAmount(1);
        product.setPrice(BigDecimal.TEN);
        return product;
    }

    public static Set<Product> getProducts(int numberOfProducts) {
        return IntStream.rangeClosed(1, numberOfProducts)
                .mapToObj(i -> getProduct(i)).collect(Collectors.toSet());
    }

    public static Set<Product> getProductsByIds(Set<Integer> ids) {
        return ids.stream().map(i -> getProduct(i)).collect(Collectors.toSet());
    }

    public static ProductDto getProductDto(Integer id) {
        ProductDto productDto = new ProductDto();
        productDto.setId(id);
        productDto.setName("productName_" + id);
        productDto.setAmount(1);
        productDto.setPrice(BigDecimal.TEN);
        return productDto;
    }

    public static ProductDto getProductDto() {
        ProductDto productDto = new ProductDto();
        productDto.setName("productName");
        productDto.setAmount(1);
        productDto.setPrice(BigDecimal.TEN);
        return productDto;
    }

    public static User getAdminUser(int id) {
        User user = new User();
        user.setId(id);
        user.setName("userName_" + id);
        user.setPassword("enecryptedPassword_" + id);
        user.setEnabled(1);
        user.setRoles(getRolesByNames(List.of(ADMIN.getName())));
        return user;
    }

    public static Order getOrder(int id, int pAmount) {
        Order order = new Order();
        order.setId(id);
        order.setName("orderName_" + id);
        order.setStatus(OrderStatus.NEW.getStatus());

        order.setUser(getAdminUser(id));
        order.setProducts(getProducts(pAmount));
        return order;
    }

    public static Order getOrder(int id, Set<Integer> pIds) {
        Order order = new Order();
        order.setId(id);
        order.setName("orderName_" + id);
        order.setStatus(OrderStatus.NEW.getStatus());

        order.setUser(getAdminUser(id));
        order.setProducts(getProductsByIds(pIds));
        return order;
    }

    public static List<Order> getOrders(int oAmount) {
        return IntStream.rangeClosed(1, oAmount)
                .mapToObj(i -> getOrder(i, 1)).collect(Collectors.toList());
    }

    public static OrderDto getOrderDto(Set<Integer> pIds) {
        OrderDto orderDto = new OrderDto();
        orderDto.setName("orderName");
        orderDto.setProductIds(pIds);
        return orderDto;
    }

    public static OrderDto getOrderDto(Integer id, Set<Integer> pIds) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(id);
        orderDto.setName("orderName_" + id);
        orderDto.setProductIds(pIds);
        return orderDto;
    }
}
