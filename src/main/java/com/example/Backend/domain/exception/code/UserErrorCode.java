package com.example.Backend.domain.exception.code;

import com.example.Backend.global.apiPayload.exception.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum UserErrorCode implements BaseErrorCode {

    // 400 계열 - 형식 오류
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "USER400_1", "이미 존재하는 이메일입니다."),
    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "USER400_2", "이메일 형식이 올바르지 않습니다."),
    INVALID_NICKNAME_FORMAT(HttpStatus.BAD_REQUEST, "USER400_3", "닉네임 형식이 잘못되었습니다."),
//    INVALID_BIRTHDATE_FORMAT(HttpStatus.BAD_REQUEST, "USER400_4", "생년월일 형식이 올바르지 않습니다. (예: yyyy-MM-dd)"),
    NOT_VALID_TYPE(HttpStatus.BAD_REQUEST, "USER400_4", "잘못된 키워드 종류입니다."),

    // 401/404 계열 - 로그인 관련 오류
    LOGIN_EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "USER404_4", "존재하지 않는 이메일입니다."),
    LOGIN_INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "USER401_1", "비밀번호가 올바르지 않습니다."),
    LOGIN_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "USER401_2", "인증되지 않은 사용자입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
