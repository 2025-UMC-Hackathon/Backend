package com.example.Backend.global.validation.validator;

import com.example.Backend.domain.enums.TagCategory;
import com.example.Backend.global.validation.annotation.TagValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TagValidator implements ConstraintValidator<TagValidation, List<String>> {

    @Override
    public boolean isValid(
            List<String> object,
            ConstraintValidatorContext constraintValidatorContext
    ) {
        return object.stream().allMatch(s ->
                Arrays.stream(TagCategory.values())
                        .anyMatch(t -> t.name().equals(s)));
    }
}
