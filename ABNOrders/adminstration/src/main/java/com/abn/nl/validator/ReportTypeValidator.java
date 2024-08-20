package com.abn.nl.validator;

import com.abn.nl.dto.ReportingType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReportTypeValidator implements ConstraintValidator<RepTypeConstraint, String> {
    private Pattern pattern;
    @Override
    public void initialize(RepTypeConstraint constraintAnnotation) {
        pattern=Pattern.
                compile(ReportingType.TOPSELLING.name()+"|"+ReportingType.LEASTSELLING.name());
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
       if(s==null)
       {
           return true;
       }
        Matcher m = pattern.matcher(s);
        return m.matches();
    }
}
