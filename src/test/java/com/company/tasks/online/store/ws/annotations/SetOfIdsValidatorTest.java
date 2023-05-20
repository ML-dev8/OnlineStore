package com.company.tasks.online.store.ws.annotations;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SetOfIdsValidatorTest {

    private SetOfIdsValidator validator;

    @BeforeAll
    void setup() {
        validator = new SetOfIdsValidator();
    }

    @Test
    void isValid_when_negativeIdProvided_then_returnFalse() {
        assertFalse(validator.isValid(Set.of(-1, 2), mock(ConstraintValidatorContext.class)));
    }

    @Test
    void isValid_when_zeroIdProvided_then_returnFalse() {
        assertFalse(validator.isValid(Set.of(0, 2), mock(ConstraintValidatorContext.class)));
    }

    @Test
    void isValid_when_nullIdProvided_then_returnFalse() {
        Set<Integer> ids = new HashSet<>(asList(null, 1));
        assertFalse(validator.isValid(ids, mock(ConstraintValidatorContext.class)));
    }

    @Test
    void isValid_when_positiveIdsProvided_then_returnTrue() {
        assertTrue(validator.isValid(Set.of(1, 2), mock(ConstraintValidatorContext.class)));
    }
}