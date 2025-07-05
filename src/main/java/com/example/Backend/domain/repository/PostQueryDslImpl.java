package com.example.Backend.domain.repository;

import com.example.Backend.domain.converter.PostConverter;
import com.example.Backend.domain.dto.PostResDTO;
import com.example.Backend.domain.entity.QPost;
import com.example.Backend.domain.entity.QPostTag;
import com.example.Backend.domain.exception.PostException;
import com.example.Backend.domain.exception.code.PostErrorCode;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostQueryDslImpl implements PostQueryDsl {

    private final JPAQueryFactory queryFactory;

    // 태그 관련된 게시글 모두 조회 (오프셋 페이지네이션)
    @Override
    public PostResDTO.PageablePost<PostResDTO.SimplePost> findPostsWithTags(
            Predicate query,
            int size
    ){
        QPost post = QPost.post;
        QPostTag postTag = QPostTag.postTag;

        // 조건에 맞는 게시글 조회
        List<PostResDTO.SimplePost> postList = queryFactory
                .from(post)
                .leftJoin(postTag).on(postTag.post.id.eq(post.id))
                .where(query)
                .orderBy(post.id.desc())
                .transform(GroupBy.groupBy(post.id).list(
                        Projections.constructor(
                                PostResDTO.SimplePost.class,
                                post.id,
                                post.title,
                                post.content,
                                post.createdAt,
                                post.user.nickname
                        )
                ));

        // 조건에 맞는 게시글 없는 경우
        if (postList.isEmpty()){
            throw new PostException(PostErrorCode.NOT_FOUND);
        }

        // 메타데이터 생성
        int pageSize = Math.min(postList.size(), size);
        String nextCursor = postList.size() > size ?
                postList.get(pageSize).id().toString() : postList.get(pageSize-1).id().toString();

        // 게시글 size 조절
        postList = postList.subList(0, pageSize);

        // 리턴
        return PostConverter.toPageablePostDTO(postList, nextCursor, pageSize);
    }
}
