package com.example.Backend.global.validation.annotation;

import com.example.Backend.global.validation.validator.TagValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TagValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TagValidation {
    String message() default "유효하지 않은 태그입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
