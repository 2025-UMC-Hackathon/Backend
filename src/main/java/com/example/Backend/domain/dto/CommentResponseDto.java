package com.example.Backend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String content;
    private String userNickname;
    private Long userId;
    private Long postId;
    private Long parentId;
    private String createdAtText; // ✅ 작성 시간 문자열
    private List<CommentResponseDto> children;

    public CommentResponseDto(Long id, String content, String userNickname, Long userId,
                              Long postId, Long parentId, List<CommentResponseDto> children, String createdAtText) {
        this.id = id;
        this.content = content;
        this.userNickname = userNickname;
        this.userId = userId;
        this.postId = postId;
        this.parentId = parentId;
        this.children = children;
        this.createdAtText = createdAtText;
    }

}

