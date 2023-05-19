package com.company.tasks.online.store.ws.dto;

import com.company.tasks.online.store.services.dto.ProductDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
public class ProductCreateRequest {
    @NotBlank(message = "Name should not be empty ")
    private String name;

    @NotNull(message = "Price should not be empty")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    @NotNull(message = "Amount should not be empty")
    @Positive(message = "Amount must be positive")
    private Integer amount;

    public ProductDto convertToProductDto() {
        ProductDto productDto = new ProductDto();
        productDto.setName(this.name);
        productDto.setPrice(this.price);
        productDto.setAmount(this.amount);
        return productDto;
    }
}
