package com.example.Backend.domain.service;

import com.example.Backend.domain.converter.UserConverter;
import com.example.Backend.domain.dto.UserRequestDTO;
import com.example.Backend.domain.entity.User;
import com.example.Backend.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    public User joinMember(UserRequestDTO.JoinDTO request) {
        //
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        User newUser= UserConverter.toUser(request);

        // 비밀번호 암호화
        newUser.encodePassword(passwordEncoder.encode(request.getPwd()));

        return userRepository.save(newUser);
    }


}
