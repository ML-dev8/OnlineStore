package com.company.tasks.online.store.ws.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

import static java.util.Objects.isNull;

public class SetOfIdsValidator implements ConstraintValidator<SetOfIdsValidation, Set<Integer>> {

    @Override
    public boolean isValid(Set<Integer> ids, ConstraintValidatorContext constraintValidatorContext) {
        for (Integer id : ids) {
            if (isNull(id) || id < 0) {
                return false;
            }
        }
        return true;
    }
}