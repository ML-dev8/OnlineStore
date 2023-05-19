package com.company.tasks.online.store.services.dto;

import com.company.tasks.online.store.entities.Product;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {
    private int id;
    private String name;
    private BigDecimal price;
    private Integer amount;

    public Product convertToProduct() {
        Product product = new Product();
        product.setName(this.name);
        product.setPrice(this.price);
        product.setAmount(this.amount);
        return product;
    }
}
