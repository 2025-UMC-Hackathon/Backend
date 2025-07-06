package com.example.Backend.domain.dto.response;

import com.example.Backend.domain.enums.PostLike;
import lombok.Builder;

import java.time.LocalDateTime;
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
            LocalDateTime createdAt,
            String title,
            String content,
            Long likes,
            Long commentCnt
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
            String nickname,
            Long commentCnt
    ){}

    // 자세한 게시글
    @Builder
    public record FullPost(
            Long id,
            String title,
            String content,
            Long likeCnt,
            Long commentCnt,
            String nickname,
            LocalDateTime createdAt
    ){}

    // 게시글 좋아요
    @Builder
    public record LikePost(
            Long postId,
            PostLike isLike
    ){}

    // 게시글 삭제
    @Builder
    public record DeletePost(
            Long postId,
            LocalDateTime deletedAt
    ){}
}
