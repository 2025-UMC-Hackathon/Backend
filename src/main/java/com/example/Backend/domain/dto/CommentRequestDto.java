package com.example.Backend.domain.dto;

import lombok.Getter;

@Getter
public class CommentRequestDto {
    private Long postId;
    private Long userId;
    private String content;
}

