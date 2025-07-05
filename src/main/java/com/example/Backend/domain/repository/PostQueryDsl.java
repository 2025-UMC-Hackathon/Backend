package com.example.Backend.domain.repository;

import com.example.Backend.domain.dto.response.PostResDTO;
import com.querydsl.core.types.Predicate;

public interface PostQueryDsl {
    // 게시글 ID 단일 조회
    PostResDTO.GetPostById getPostById(
            Long postId
    );

    // 태그 관련된 게시글 모두 조회 (커서 페이지네이션)
    PostResDTO.PageablePost<PostResDTO.SimplePost> findPostsWithTags(
            Predicate query,
            int size
    );

    PostResDTO.PageablePost<PostResDTO.FullPost> getMyLikePost(
            Predicate builder,
            int size
    );

    PostResDTO.PageablePost<PostResDTO.FullPost> getMyPosts(
            Predicate query,
            int size
    );
}
