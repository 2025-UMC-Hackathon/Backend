package com.example.Backend.domain.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class PostResDTO {

    // 게시글 생성
    @Builder
    public record CreatePost(
            Long postId,
            LocalDateTime createdAt
    ){}

    // 게시글ID로 게시글 조회
    @Builder
    public record GetPostById(
            Long id,
            String nickname,
            LocalDate writeDate,
            LocalTime writeTime,
            String title,
            String content,
            Long likes
    ){}

    // 커서 페이지네이션
    @Builder
    public record PageablePost<T>(
            List<T> posts,
            String cursor,
            int pageSize
    ){}

    // 간단 게시글
    @Builder
    public record SimplePost(
            Long id,
            String title,
            String content,
            LocalDateTime createdAt,
            String nickname
    ){}

    // 자세한 게시글
    @Builder
    public record FullPost(
            String title,
            String content,
            Long likeCnt,
            Long commentCnt,
            String nickname,
            LocalDateTime createdAt
    ){}
}
