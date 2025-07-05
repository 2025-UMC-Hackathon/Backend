package com.example.Backend.global.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthUser {

    private Long userId;
    private String email;
    private String password;
}
