package com.example.Backend.domain.dto.request;

import com.example.Backend.global.validation.annotation.TagValidation;
import com.example.Backend.global.validation.annotation.TypeValid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class PostReqDTO {

    // 게시글 생성
    public record CreatePost(
            @NotEmpty(message = "제목은 필수로 입력해야 합니다.")
            String title,
            @NotEmpty(message = "내용은 필수로 입력해야 합니다.")
            String content,
            @TypeValid
            @NotEmpty(message = "장애유형은 필수로 입력해야 합니다.")
            List<String> types,
            @TagValidation
            @NotEmpty(message = "고민은 필수로 입력해야 합니다.")
            List<String> tags
    ){}

    // 게시글ID로 게시글 조회
    public record GetPostById(
            Long postId
    ){}
}
