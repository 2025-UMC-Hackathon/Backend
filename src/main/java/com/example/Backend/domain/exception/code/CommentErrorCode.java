package com.example.Backend.domain.exception.code;

import com.example.Backend.global.apiPayload.exception.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommentErrorCode implements BaseErrorCode {

    COMMENT_NOT_FOUND(HttpStatus.NOT_ACCEPTABLE,
            "COMMENT406_0",
            "해당 댓글이 존재하지 않습니다."),

    COMMENTS_EMPTY(HttpStatus.NOT_ACCEPTABLE,
            "COMMENT406_1",
            "해당 게시글에는 댓글이 존재하지 않습니다."),

    USER_NOT_FOUND(HttpStatus.NOT_ACCEPTABLE,
            "COMMENT406_2",
            "해당 유저가 존재하지 않습니다."),

    PARENT_COMMENT_NOT_FOUND(HttpStatus.NOT_ACCEPTABLE,
            "COMMENT406_3",
            "부모 댓글이 존재하지 않습니다."),

    EMPTY_CONTENT(HttpStatus.BAD_REQUEST,
            "COMMENT400_2", "댓글 내용은 비어 있을 수 없습니다."),

    INVALID_JSON_FORMAT(HttpStatus.BAD_REQUEST,
            "COMMENT400_1", "요청 형식이 올바르지 않습니다."),


    INVALID_ENCODING(HttpStatus.BAD_REQUEST,
                     "COMMENT400_0", "요청 데이터의 인코딩이 잘못되었습니다. UTF-8 형식인지 확인해주세요."),

    INVALID_TYPE(HttpStatus.BAD_REQUEST,
            "COMMENT400_3", "잘못된 요청 타입입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
