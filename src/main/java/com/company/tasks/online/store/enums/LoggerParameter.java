package com.company.tasks.online.store.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoggerParameter {
    UNIQUE_ID("uniqID");

    private final String name;
}