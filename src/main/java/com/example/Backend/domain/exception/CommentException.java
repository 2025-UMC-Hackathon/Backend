package com.example.Backend.domain.exception;

import com.example.Backend.global.apiPayload.exception.CustomException;
import com.example.Backend.global.apiPayload.exception.code.BaseErrorCode;

public class CommentException extends CustomException {
  public CommentException(BaseErrorCode errorCode) {
    super(errorCode);
  }
}
