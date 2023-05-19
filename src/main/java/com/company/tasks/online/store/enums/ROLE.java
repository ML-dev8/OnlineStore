package com.company.tasks.online.store.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ROLE {
    ADMIN("ADMIN"),
    USER("USER");

    private final String name;
}
