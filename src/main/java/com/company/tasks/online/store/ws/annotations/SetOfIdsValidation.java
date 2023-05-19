package com.company.tasks.online.store.ws.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

@Target({FIELD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = SetOfIdsValidator.class)
public @interface SetOfIdsValidation {
    String message() default "Invalid ids provided";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}