package com.example.Backend.domain.exception.code;

import com.example.Backend.global.apiPayload.exception.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum UserErrorCode implements BaseErrorCode {

    // 404 계열 - 잘못된 enum 값
    INVALID_DISABILITY_TYPE(HttpStatus.NOT_FOUND, "USER404_1", "지정된 장애 유형(enum) 값이 아님"),
    INVALID_USER_TYPE(HttpStatus.NOT_FOUND, "USER404_2", "지정된 사용자 유형(enum) 값이 아님"),
    INVALID_DISABILITY_LEVEL(HttpStatus.NOT_FOUND, "USER404_3", "지정된 장애 정도(enum) 값이 아님"),

    // 400 계열 - 형식 오류
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "USER400_1", "이미 존재하는 이메일입니다."),
    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "USER400_2", "이메일 형식이 올바르지 않습니다."),
    INVALID_NICKNAME_FORMAT(HttpStatus.BAD_REQUEST, "USER400_3", "닉네임 형식이 잘못되었습니다."),
    //NVALID_BIRTHDATE_FORMAT(HttpStatus.BAD_REQUEST, "USER400_4", "생년월일 형식이 올바르지 않습니다. (예: yyyy-MM-dd)"),

    // 예시용 기타 코드 (추후 추가 가능)
    NOT_VALID_TYPE(HttpStatus.BAD_REQUEST, "USER400_4", "잘못된 키워드 종류입니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
