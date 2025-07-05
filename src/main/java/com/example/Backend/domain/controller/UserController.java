package com.example.Backend.domain.controller;

import com.example.Backend.domain.dto.UserRequestDTO;

import com.example.Backend.domain.dto.UserResponseDTO;
import com.example.Backend.domain.entity.User;
import com.example.Backend.domain.service.UserService;
import com.example.Backend.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.models.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/api/signup")
    public CustomResponse<String> signUp(@RequestBody @Valid UserRequestDTO.JoinDTO dto) {
        // 1. 회원가입 처리
        User user = userService.joinMember(dto);

        // 2. 단순 메시지 응답
        return CustomResponse.ok("회원가입 완료");
    }
}
