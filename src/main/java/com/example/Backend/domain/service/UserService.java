package com.example.Backend.domain.service;

import com.example.Backend.domain.converter.UserConverter;
import com.example.Backend.domain.dto.UserRequestDTO;
import com.example.Backend.domain.entity.User;
import com.example.Backend.domain.enums.DisabilityLevel;
import com.example.Backend.domain.enums.DisabilityType;
import com.example.Backend.domain.enums.UserType;
import com.example.Backend.domain.exception.UserException;
import com.example.Backend.domain.exception.code.UserErrorCode;
import com.example.Backend.domain.repository.UserRepository;
import com.example.Backend.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public User joinMember(UserRequestDTO.JoinDTO request) {

// ✅ 1. 이메일 중복 확인
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserException(UserErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // ✅ 2. 이메일 형식 확인 (정규식 등 사용 예시)
        if (!request.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new UserException(UserErrorCode.INVALID_EMAIL_FORMAT);
        }

        // ✅ 3. 닉네임 형식 확인 (예: 한글/영문/숫자 2~10자)
        if (!request.getNickname().matches("^[가-힣a-zA-Z0-9]{2,10}$")) {
            throw new UserException(UserErrorCode.INVALID_NICKNAME_FORMAT);
        }


        User newUser= UserConverter.toUser(request);

        // 비밀번호 암호화
        newUser.encodePassword(passwordEncoder.encode(request.getPwd()));

        return userRepository.save(newUser);
    }

    public String login(UserRequestDTO.LoginDTO dto) {
        // ✅ 1. 이메일 형식 확인 (정규식 등 사용 예시)
        if (!dto.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new UserException(UserErrorCode.INVALID_EMAIL_FORMAT);
        }
        // ✅2. 사용자 존재 여부 확인
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserException(UserErrorCode.LOGIN_EMAIL_NOT_FOUND));

        // ✅3. 비밀번호 검증
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());

        // ✅4. 인증 검증
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new UserException(UserErrorCode.LOGIN_INVALID_PASSWORD);
        } catch (AuthenticationException e) {
            throw new UserException(UserErrorCode.LOGIN_UNAUTHORIZED); // 그 외 인증 실패
        }

        // 3. 인증 성공 시 SecurityContext에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 4. JWT 토큰 생성
        return jwtTokenProvider.createToken(user.getEmail());
    }


}
