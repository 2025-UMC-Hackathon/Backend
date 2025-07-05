package com.example.Backend.domain.dto;

import com.example.Backend.domain.enums.DisabilityLevel;
import com.example.Backend.domain.enums.DisabilityType;
import com.example.Backend.domain.enums.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class UserRequestDTO {
    @Getter
    @Setter
    public static class JoinDTO{
        @NotBlank
        private String nickname;
        @NotBlank
        @Email
        private String email;
        @NotBlank
        private String pwd;
        @NotNull
        private DisabilityType disabilityType;
        @NotNull
        private LocalDate birthDate;
        @NotNull
        private UserType userType;
        private DisabilityLevel disabilityLevel;  // NULL 허용 필드
    }


}
