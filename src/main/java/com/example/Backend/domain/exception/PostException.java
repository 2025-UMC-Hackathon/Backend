package com.example.Backend.domain.exception;

import com.example.Backend.global.apiPayload.exception.CustomException;
import com.example.Backend.global.apiPayload.exception.code.BaseErrorCode;

public class PostException extends CustomException {
    public PostException(BaseErrorCode code) {
        super(code);
    }
}
