package com.company.tasks.online.store.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    NEW("N"),
    PROCESSED("P");

    private final String status;
}