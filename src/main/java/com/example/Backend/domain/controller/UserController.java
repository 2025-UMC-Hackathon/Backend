package com.example.Backend.domain.controller;

import ch.qos.logback.core.status.ErrorStatus;
import com.example.Backend.domain.dto.UserRequestDTO;

import com.example.Backend.domain.dto.UserResponseDTO;
import com.example.Backend.domain.entity.User;
import com.example.Backend.domain.repository.UserRepository;
import com.example.Backend.domain.service.UserService;
import com.example.Backend.global.apiPayload.CustomResponse;
import com.example.Backend.global.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.models.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(
            summary = "회원가입 API By 박지영",
            description = """
        이메일, 비밀번호, 생년월일 등을 받아 회원가입을 수행합니다.

        장애유형: `시각`, `청각`, `신체적장애`, `지적장애`, `발달장애`, `기타`,  
        장애인 구분: `중증`, `경증`,  
        사용자 유형: `성인장애인`, `장애인자녀를둔부모` 로 입력해주세요.
        """

    )

   @PostMapping("/api/signup")
    public CustomResponse<String> signUp(
            @RequestBody UserRequestDTO.JoinDTO dto
    ) {
        // 1. 회원가입 처리
        User user = userService.joinMember(dto);

        // 2. 단순 메시지 응답
        return CustomResponse.ok("회원가입 완료");
    }

    // 로그인 처리 (Spring Security 사용 시 생략 가능)
    @PostMapping("/api/login")
    @Operation(summary = "로그인 API By 박지영", description = "사용자가 입력한 이메일, 비밀번호로 로그인 요청합니다.")
    public CustomResponse<String> login(@RequestBody @Valid UserRequestDTO.LoginDTO dto) {
        String token = userService.login(dto);
        return CustomResponse.ok(token);
    }

}
