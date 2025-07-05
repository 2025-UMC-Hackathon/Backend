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
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

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

        // ✅ 4. 장애 유형 enum 값 확인
        try {
            DisabilityType.valueOf(request.getDisabilityType().name().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UserException(UserErrorCode.INVALID_DISABILITY_TYPE);
        }

        // ✅ 5. 사용자 유형 enum 값 확인
        try {
            UserType.valueOf(request.getUserType().name().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UserException(UserErrorCode.INVALID_USER_TYPE);
        }

        // ✅ 6. 장애 정도 enum 값 확인
        try {
            DisabilityLevel.valueOf(request.getDisabilityLevel().name().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UserException(UserErrorCode.INVALID_DISABILITY_LEVEL);
        }


        User newUser= UserConverter.toUser(request);

        // 비밀번호 암호화
        newUser.encodePassword(passwordEncoder.encode(request.getPwd()));

        return userRepository.save(newUser);
    }


}
