package com.company.tasks.online.store.ws.dto;

import com.company.tasks.online.store.exceptions.BadRequestException;
import com.company.tasks.online.store.services.dto.OrderDto;
import com.company.tasks.online.store.ws.annotations.SetOfIdsValidation;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class OrderCreateRequest {
    @NotBlank(message = "Name must not be empty ")
    private String name;
    @NotNull(message = "productIds must not be null")
    @NotEmpty(message = "productIds must not be empty")
    @SetOfIdsValidation(message = "Invalid productIds")
    private Set<Integer> productIds;

    public OrderDto convertToOrderDto() throws BadRequestException {
        OrderDto orderDto = new OrderDto();
        orderDto.setName(this.name);
        orderDto.setProductIds(this.productIds);
        return orderDto;
    }

}
