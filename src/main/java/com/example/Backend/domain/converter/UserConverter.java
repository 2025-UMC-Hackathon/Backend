package com.example.Backend.domain.converter;

import com.example.Backend.domain.dto.UserResponseDTO;
import com.example.Backend.domain.entity.User;
import com.example.Backend.domain.dto.UserRequestDTO;

import java.time.LocalDateTime;

public class UserConverter {
    // 회원가입 응답용 DTO
    public static UserResponseDTO.JoinResultDTO toJoinResultDTO(User user) {
        return UserResponseDTO.JoinResultDTO.builder()
                .id(user.getId())
                .build();
    }
    // 회원가입 요청 DTO
    public static User toUser(UserRequestDTO.JoinDTO request) {
        return User.builder()
                .nickname(request.getNickname())
                .email(request.getEmail())
                .pwd(request.getPwd())  // 암호화 필요
                .birthDate(request.getBirthDate())
                .disabilityType(request.getDisabilityType())
                .disabilityLevel(request.getDisabilityLevel())
                .userType(request.getUserType())
                .build();
    }
}
