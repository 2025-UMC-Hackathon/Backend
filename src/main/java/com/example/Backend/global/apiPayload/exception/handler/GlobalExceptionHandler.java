package com.example.Backend.global.apiPayload.exception.handler;

import com.example.Backend.domain.exception.CommentException;
import com.example.Backend.domain.exception.code.CommentErrorCode;
import com.example.Backend.global.apiPayload.CustomResponse;
import com.example.Backend.global.apiPayload.exception.CustomException;
import com.example.Backend.global.apiPayload.exception.code.BaseErrorCode;
import com.example.Backend.global.apiPayload.exception.code.GeneralErrorCode;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 컨트롤러 메서드에서 @Valid 어노테이션을 사용하여 DTO의 유효성 검사를 수행
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<CustomResponse<Map<String, String>>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex
    ) {
        // 검사에 실패한 필드와 그에 대한 메시지를 저장하는 Map
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        BaseErrorCode validationErrorCode = GeneralErrorCode.VALIDATION_FAILED; // BaseErrorCode로 통일
        CustomResponse<Map<String, String>> errorResponse = CustomResponse.onFailure(
                validationErrorCode,
                errors
        );
        // 에러 코드, 메시지와 함께 errors를 반환
        return ResponseEntity.status(validationErrorCode.getHttpStatus()).body(errorResponse);
    }

    // 쿼리 파라미터 검증
    @ExceptionHandler(HandlerMethodValidationException.class)
    protected ResponseEntity<CustomResponse<Map<String, String>>> handleHandlerMethodValidationException(
            HandlerMethodValidationException ex
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getParameterValidationResults().forEach(result ->
                errors.put(result.getMethodParameter().getParameterName(), result.getResolvableErrors().get(0).getDefaultMessage()));
        BaseErrorCode validationErrorCode = GeneralErrorCode.VALIDATION_FAILED; // BaseErrorCode로 통일
        CustomResponse<Map<String, String>> errorResponse = CustomResponse.onFailure(
                validationErrorCode,
                errors
        );
        // 에러 코드, 메시지와 함께 errors를 반환
        return ResponseEntity.status(validationErrorCode.getHttpStatus()).body(errorResponse);

    }

    // 요청 파라미터가 없을 때 발생하는 예외 처리
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<CustomResponse<String>> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex
    ) {

        log.warn("[ MissingRequestParameterException ]: 필요한 파라미터가 요청에 없습니다.");
        BaseErrorCode validationErrorCode = GeneralErrorCode.VALIDATION_FAILED; // BaseErrorCode로 통일
        CustomResponse<String> errorResponse = CustomResponse.onFailure(
                validationErrorCode,
                ex.getParameterName()+" 파라미터가 없습니다."
        );
        // 에러 코드, 메시지와 함께 errors를 반환
        return ResponseEntity.status(validationErrorCode.getHttpStatus()).body(errorResponse);
    }

    // ConstraintViolationException 핸들러
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<CustomResponse<Map<String, String>>> handleConstraintViolationException(
            ConstraintViolationException ex
    ) {
        // 제약 조건 위반 정보를 저장할 Map
        Map<String, String> errors = new HashMap<>();

        ex.getConstraintViolations().forEach(violation -> {
            String propertyPath = violation.getPropertyPath().toString();
            // 마지막 필드명만 추출 (예: user.name -> name)
            String fieldName = propertyPath.contains(".") ?
                    propertyPath.substring(propertyPath.lastIndexOf(".") + 1) : propertyPath;

            errors.put(fieldName, violation.getMessage());
        });

        BaseErrorCode constraintErrorCode = GeneralErrorCode.VALIDATION_FAILED;
        CustomResponse<Map<String, String>> errorResponse = CustomResponse.onFailure(
                constraintErrorCode,
                errors
        );

        log.warn("[ ConstraintViolationException ]: Constraint violations detected");

        return ResponseEntity.status(constraintErrorCode.getHttpStatus()).body(errorResponse);
    }

    // 애플리케이션에서 발생하는 커스텀 예외를 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomResponse<Void>> handleCustomException(
            CustomException ex
    ) {
        //예외가 발생하면 로그 기록
        log.warn("[ CustomException ]: {}", ex.getCode().getMessage());
        //커스텀 예외에 정의된 에러 코드와 메시지를 포함한 응답 제공
        return ResponseEntity.status(ex.getCode().getHttpStatus())
                .body(CustomResponse.onFailure(
                                ex.getCode(),
                                null
                        )
                );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<CustomResponse<String>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex
    ) {
        log.warn("[ HttpMessageNotReadableException ]: {}", ex.getMessage());

        // 커스텀 메시지 설정 (enum 파싱 실패 메시지를 포함할 수도 있음)
        String message = "요청 JSON 형식이 잘못되었습니다. enum 값이 유효한지 확인하세요.";

        // 예: USER400_9 (GeneralErrorCode로 따로 빼도 됩니다)
        BaseErrorCode errorCode = GeneralErrorCode.VALIDATION_FAILED;

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(CustomResponse.onFailure(errorCode, message));
    }



    // 그 외의 정의되지 않은 모든 예외 처리
    @ExceptionHandler({Exception.class})
    public ResponseEntity<CustomResponse<String>> handleAllException(
            Exception ex
    ) {
        log.error("[WARNING] Internal Server Error : {} ", ex.getMessage());
        BaseErrorCode errorCode = GeneralErrorCode.INTERNAL_SERVER_ERROR_500;
        CustomResponse<String> errorResponse = CustomResponse.onFailure(
                errorCode,
                null
        );
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(errorResponse);
    }

    // JSON 파싱 실패 시 UTF-8 인코딩 오류와 형식 오류를 구분하여 처리

    //JSON 파싱 중 타입 오류 발생 시
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomResponse<String>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.warn("[BAD REQUEST] JSON 파싱 에러: {}", ex.getMessage());
        CommentErrorCode errorCode = CommentErrorCode.INVALID_JSON_FORMAT;

        CustomResponse<String> errorResponse = CustomResponse.onFailure(
                errorCode,
                null
        );

        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CustomResponse<String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.warn("[BAD REQUEST] 잘못된 타입 입력: {}", ex.getMessage());
        CommentErrorCode errorCode = CommentErrorCode.INVALID_TYPE;

        CustomResponse<String> errorResponse = CustomResponse.onFailure(
                errorCode,
                null
        );

        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }

}
