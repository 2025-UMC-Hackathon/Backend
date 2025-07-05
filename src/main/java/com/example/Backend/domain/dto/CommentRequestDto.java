package com.example.Backend.domain.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentRequestDto {
    @Valid
    private Integer postId;
    private Long userId;
    private String content;
    private Long parentId;
}

