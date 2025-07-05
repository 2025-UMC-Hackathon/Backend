package com.example.Backend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentRequestDto {
    private Long postId;
    private Long userId;
    private String content;
    private Long parentId;
}

