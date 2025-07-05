package com.example.Backend.domain.exception;

import com.example.Backend.global.apiPayload.exception.CustomException;
import com.example.Backend.global.apiPayload.exception.code.BaseErrorCode;

public class UserException extends CustomException {
    public UserException(BaseErrorCode code) {
      super(code);
    }
}
