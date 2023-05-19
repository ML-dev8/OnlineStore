package com.company.tasks.online.store.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestPojo {
    private String fullName;
    private int age;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestPojo testPojo = (TestPojo) o;
        return age == testPojo.age && Objects.equals(fullName, testPojo.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, age);
    }
}