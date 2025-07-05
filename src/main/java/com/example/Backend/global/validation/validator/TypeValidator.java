package com.example.Backend.global.validation.validator;

import com.example.Backend.domain.enums.TypeCategory;
import com.example.Backend.global.validation.annotation.TypeValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TypeValidator implements ConstraintValidator<TypeValid, List<String>> {

    @Override
    public boolean isValid(
            List<String> object,
            ConstraintValidatorContext constraintValidatorContext
    ) {
        return object.stream().allMatch(s ->
                Arrays.stream(TypeCategory.values())
                        .anyMatch(t -> t.name().equals(s)));
    }
}