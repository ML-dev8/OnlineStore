package com.company.tasks.online.store.services.dto;

import lombok.Data;

import java.util.Set;

@Data
public class OrderDto {
    private int id;
    private String name;
    private Set<Integer> productIds;
}
