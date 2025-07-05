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
//
//    // 로그인 페이지
//    @GetMapping("/api/login")
//    @Operation(summary = "로그인 페이지", description = "사용자가 로그인 페이지로 이동")
//    public String loginPage() {
//        return "login"; // login.html 페이지
//    }
//
//    // 회원가입 페이지
//    @GetMapping("/api/signup")
//    @Operation(summary = "회원가입 페이지", description = "사용자가 회원가입 페이지로 이동")
//    public String signupPage() {
//        return "signup"; // signup.html 페이지
//    }

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
    @Operation(summary = "로그인 API", description = "사용자가 로그인 요청")

    public CustomResponse<String> login(@RequestBody @Valid UserRequestDTO.LoginDTO dto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 2. 로그인 성공 시 SecurityContext에 인증 정보를 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 4. 사용자 정보 가져오기
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("로그인 실패: 해당 이메일의 사용자를 찾을 수 없습니다."));

        // 5. JWT 토큰 생성
        String token = jwtTokenProvider.createToken(user.getEmail());

        // 6. 토큰 응답
        return CustomResponse.ok(token); // 또는 CustomResponse.ok("Bearer " + token);
    }

}
