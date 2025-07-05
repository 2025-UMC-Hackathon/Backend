package com.example.Backend.domain.converter;

import com.example.Backend.domain.dto.PostReqDTO;
import com.example.Backend.domain.dto.PostResDTO;
import com.example.Backend.domain.entity.Post;
import com.example.Backend.domain.entity.User;

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

    // Post -> 게시글 단일 조회 dto
    public static PostResDTO.GetPostById getPostById(
            Post post
    ){
        return PostResDTO.GetPostById.builder()
                .id(post.getId())
                .nickname(post.getUser().getNickname())
                .writeDate(post.getCreatedAt().toLocalDate())
                .writeTime(post.getCreatedAt().toLocalTime())
                .title(post.getTitle())
                .content(post.getContent())
                .likes(post.getLikeCnt())
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
}
