package com.abn.nl.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MobileNumberValidator implements ConstraintValidator<MobileNumberConstraint, String> {

    @Override
    public void initialize(MobileNumberConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s != null && s.matches("^\\+\\d+$")
                && (s.length() > 0) && (s.length() > 10);
    }
}
