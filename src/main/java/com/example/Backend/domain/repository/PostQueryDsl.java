package com.example.Backend.domain.repository;

import com.example.Backend.domain.dto.PostResDTO;
import com.querydsl.core.types.Predicate;

public interface PostQueryDsl {
    // 태그 관련된 게시글 모두 조회 (커서 페이지네이션)
    PostResDTO.PageablePost<PostResDTO.SimplePost> findPostsWithTags(
            Predicate query,
            int size
    );

    PostResDTO.PageablePost<PostResDTO.FullPost> getMyLikePost(
            Predicate builder,
            int size
    );
}
