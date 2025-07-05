package com.example.Backend.global.validation.annotation;

import com.example.Backend.global.validation.validator.TypeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TypeValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TypeValid {
    String message() default "유효하지 않은 타입입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}