package com.example.Backend.global.apiPayload.exception;

import com.example.Backend.global.apiPayload.exception.code.BaseErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{

    private final BaseErrorCode code;

    public CustomException(BaseErrorCode errorCode) {
        this.code = errorCode;
    }
}