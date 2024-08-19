package com.abn.nl.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = MobileNumberValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MobileNumberConstraint {

    String message() default "Invalid phone number.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}