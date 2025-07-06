package com.example.Backend.domain.service;

import com.example.Backend.domain.converter.PostConverter;
import com.example.Backend.domain.converter.PostTagConverter;
import com.example.Backend.domain.converter.TagConverter;
import com.example.Backend.domain.converter.UserLikeConverter;
import com.example.Backend.domain.dto.request.PostReqDTO;
import com.example.Backend.domain.dto.response.PostResDTO;
import com.example.Backend.domain.entity.*;
import com.example.Backend.domain.enums.PostLike;
import com.example.Backend.domain.exception.PostException;
import com.example.Backend.domain.exception.code.PostErrorCode;
import com.example.Backend.domain.repository.*;
import com.example.Backend.global.auth.AuthUser;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;
    private final UserLikeRepository userLikeRepository;
    private final UserRepository userRepository;

    // 게시글ID로 게시글 조회 (단일)
    public PostResDTO.GetPostById getPostById(
            Long postId
    ){
        // 조회
        postRepository.findPostById(postId).orElseThrow(() ->
                new PostException(PostErrorCode.NOT_FOUND));
        return postRepository.getPostById(postId);
    }

    // 태그와 관련된 게시글 모두 조회 (커서기반 페이지네이션)
    public PostResDTO.PageablePost<PostResDTO.SimplePost> getPostsWithTags(
            List<String> tags,
            List<String> types,
            String cursor,
            int size
    ){
        // 객체 생성
        QPostTag postTag = QPostTag.postTag;
        QPost post = QPost.post;

        // 태그 정제
        List<Tag> tagList = tagRepository.findByContentIn(tags);
        List<Tag> typesList = tagRepository.findByContentIn(types);

        // 조건 설정
        BooleanBuilder builder = new BooleanBuilder();
        if (!tagList.isEmpty() && !typesList.isEmpty()) {
            builder.and(postTag.tag.in(tagList))
                    .or(postTag.tag.in(typesList));
        } else if (!typesList.isEmpty()){
            builder.and(postTag.tag.in(typesList));
        } else if (!tagList.isEmpty()){
            builder.and(postTag.tag.in(tagList));
        }

        if (!cursor.equals("-1")){
            try {
                builder.and(post.id.loe(Long.parseLong(cursor)));
            } catch (NumberFormatException e){
                throw new PostException(PostErrorCode.NOT_VALID_CURSOR);
            }
        }
        return postRepository.findPostsWithTags(builder, size);
    }

    // 게시글 좋아요
    @Transactional
    public PostResDTO.LikePost LikePost(
            AuthUser authUser,
            Long postId
    ){

        // 유저 정보 조회
        User user = userRepository.findUserById(authUser.getUserId()).orElseThrow(() ->
                new PostException(PostErrorCode.USER_NOT_FOUND));

        // 게시글 존재 여부 확인
        Post post = postRepository.findPostById(postId).orElseThrow(() ->
                new PostException(PostErrorCode.NOT_FOUND));

        // 현재 반응 조회
        UserLike reaction = userLikeRepository.findByUserIdAndPostId(user.getId(), postId).orElse(null);

        // 좋아요 누른 적이 없으면 생성
        if (reaction == null) {

            reaction = userLikeRepository.save(
                    UserLikeConverter.toUserLike(user, post, PostLike.LIKE));
        } else if (reaction.getIsLike().equals(PostLike.LIKE)) {

            reaction.updateLike(PostLike.UNLIKE);
            post.updateLikeCount(post.getLikeCnt() - 1);
        } else {

            reaction.updateLike(PostLike.LIKE);
        }

        if (reaction.getIsLike().equals(PostLike.LIKE)) {

            post.updateLikeCount(post.getLikeCnt() + 1);
        }

        return PostConverter.toLikePost(reaction);
    }

    // 게시글 생성
    @Transactional
    public PostResDTO.CreatePost createPost(
            AuthUser authUser,
            PostReqDTO.CreatePost dto
    ){

        // 유저 정보 불러오기
        User user = userRepository.findUserById(authUser.getUserId()).orElseThrow(() ->
                new PostException(PostErrorCode.USER_NOT_FOUND));
        // 게시글 저장
        Post post = postRepository.save(PostConverter.toPost(dto, user));

        LocalDateTime now = LocalDateTime.now();

        // 고민 태그 생성 : 기존 태그 불러오기 + 없는 태그 저장하기
        List<Tag> foundTags = tagRepository.findByContentIn(dto.tags().stream().distinct().toList());
        Map<String, Tag> tagMap = foundTags.stream()
                .distinct()
                .collect(Collectors.toMap(Tag::getContent, Function.identity()));

        List<Tag> tags = new ArrayList<>();
        for (String tagName : dto.tags().stream().distinct().toList()) {
            Tag tag = tagMap.get(tagName);
            if (tag == null) {
                tag = tagRepository.save(TagConverter.toTag(tagName));
            }
            tags.add(tag);
        }

        // 게시글 <-> 태그 연동
        List<PostTag> postTags = new ArrayList<>();
        for (Tag tag : tags) {
            postTags.add(PostTagConverter.toPostTag(post, tag));
        }
        postTagRepository.saveAll(postTags);

        // 유형 태그 생성 : 기존 태그 불러오기 + 없는 태그 저장하기
        List<Tag> foundTypes = tagRepository.findByContentIn(dto.types().stream().distinct().toList());
        Map<String, Tag> typeMap = foundTypes.stream()
                .distinct()
                .collect(Collectors.toMap(Tag::getContent, Function.identity()));

        List<Tag> types = new ArrayList<>();
        for (String typeName : dto.types().stream().distinct().toList()) {
            Tag type = typeMap.get(typeName);
            if (type == null) {
                type = tagRepository.save(TagConverter.toTag(typeName));
            }
            types.add(type);
        }

        // 게시글 <-> 태그 연동
        List<PostTag> postTypes = new ArrayList<>();
        for (Tag tag : types) {
            postTypes.add(PostTagConverter.toPostTag(post, tag));
        }
        postTagRepository.saveAll(postTypes);

        // 리턴
        return PostConverter.toCreatePostDTO(post, now);
    }

    // 내가 좋아요 누른 게시글 조회
    public PostResDTO.PageablePost<PostResDTO.FullPost> getMyLikePost(
            AuthUser user,
            String cursor,
            int size
    ){

        BooleanBuilder builder = new BooleanBuilder();
        QPost post = QPost.post;
        QUserLike userLike = QUserLike.userLike;

        builder.and(userLike.user.id.eq(user.getUserId()))
                .and(userLike.isLike.eq(PostLike.LIKE));

        if (!cursor.equals("-1")) {
            try {
                builder.and(post.id.loe(Long.parseLong(cursor)));
            } catch (NumberFormatException e){
                throw new PostException(PostErrorCode.NOT_VALID_CURSOR);
            }
        }

        return postRepository.getMyLikePost(builder, size);
    }

    // 내가 작성한 게시글 조회
    public PostResDTO.PageablePost<PostResDTO.FullPost> getMyPosts(
            AuthUser user,
            String cursor,
            int size
    ) {

        BooleanBuilder builder = new BooleanBuilder();
        QPost post = QPost.post;

        builder.and(post.user.id.eq(user.getUserId()));
        if (!cursor.equals("-1")) {
            try {
                builder.and(post.id.loe(Long.parseLong(cursor)));
            } catch (NumberFormatException e){
                throw new PostException(PostErrorCode.NOT_VALID_CURSOR);
            }
        }

        return postRepository.getMyPosts(builder, size);
    }

    // 게시글 삭제
    public PostResDTO.DeletePost deletePost(
            Long postId,
            AuthUser authUser
    ){

        // 게시글 존재 여부 확인
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new PostException(PostErrorCode.NOT_FOUND));

        // 작성자인지 확인
        if (!post.getUser().getId().equals(authUser.getUserId())) {
            throw new PostException(PostErrorCode.USER_NOT_MATCH);
        }

        // 삭제: 게시글 -> 게시글 태그
        postRepository.delete(post);

        LocalDateTime now = LocalDateTime.now();
        return PostConverter.toDeletePost(postId, now);
    }
}
