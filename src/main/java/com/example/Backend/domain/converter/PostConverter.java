package com.example.Backend.domain.converter;

import com.example.Backend.domain.dto.request.PostReqDTO;
import com.example.Backend.domain.dto.response.PostResDTO;
import com.example.Backend.domain.entity.Post;
import com.example.Backend.domain.entity.User;
import com.example.Backend.domain.entity.UserLike;

import java.time.LocalDateTime;
import java.util.List;

public class PostConverter {

    // dto -> 게시글
    public static Post toPost(
            PostReqDTO.CreatePost dto,
            User user
    ){
        return Post.builder()
                .title(dto.title())
                .content(dto.content())
                .user(user)
                .build();
    }

    // Post -> 게시글 생성 dto
    public static PostResDTO.CreatePost toCreatePostDTO(
            Post post,
            LocalDateTime now
    ){
        return PostResDTO.CreatePost.builder()
                .postId(post.getId())
                .createdAt(now)
                .build();
    }

    // post, offset, pageSize -> PageablePost
    public static <T>PostResDTO.PageablePost<T> toPageablePostDTO(
            List<T> post,
            String cursor,
            int pageSize
    ){
        return PostResDTO.PageablePost.<T>builder()
                .posts(post)
                .cursor(cursor)
                .pageSize(pageSize)
                .build();
    }

    // 게시글 좋아요
    public static PostResDTO.LikePost toLikePost(
            UserLike userLike
    ){
        return PostResDTO.LikePost.builder()
                .postId(userLike.getPost().getId())
                .isLike(userLike.getIsLike())
                .build();
    }

    // 게시글 삭제
    public static PostResDTO.DeletePost toDeletePost(
            Long postId,
            LocalDateTime now
    ){
        return PostResDTO.DeletePost.builder()
                .postId(postId)
                .deletedAt(now)
                .build();
    }
}
