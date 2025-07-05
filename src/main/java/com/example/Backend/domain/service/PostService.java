package com.example.Backend.domain.service;

import com.example.Backend.domain.converter.PostConverter;
import com.example.Backend.domain.converter.PostTagConverter;
import com.example.Backend.domain.converter.TagConverter;
import com.example.Backend.domain.dto.PostReqDTO;
import com.example.Backend.domain.dto.PostResDTO;
import com.example.Backend.domain.entity.*;
import com.example.Backend.domain.exception.PostException;
import com.example.Backend.domain.exception.code.PostErrorCode;
import com.example.Backend.domain.repository.PostRepository;
import com.example.Backend.domain.repository.PostTagRepository;
import com.example.Backend.domain.repository.TagRepository;
import com.example.Backend.domain.repository.UserRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    private final UserRepository userRepository;

    // 게시글ID로 게시글 조회 (단일)
    public PostResDTO.GetPostById getPostById(
            Long postId
    ){
        // 조회하고 리턴
        return PostConverter.getPostById(postRepository.findPostById(postId));
    }

    // 태그와 관련된 게시글 모두 조회 (커서기반 페이지네이션)
    public PostResDTO.PageablePost<PostResDTO.SimplePost> getPostsWithTags(
            List<String> tags,
            String cursor,
            int size
    ){
        // 객체 생성
        QPostTag postTag = QPostTag.postTag;
        QPost post = QPost.post;

        // 태그 정제
        List<Tag> tagList = tagRepository.findByContentIn(tags);

        // 조건 설정
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(postTag.tag.in(tagList));

        if (!cursor.equals("-1")){
            try {
                builder.and(post.id.loe(Long.parseLong(cursor)));
            } catch (NumberFormatException e){
                throw new PostException(PostErrorCode.NOT_VALID_CURSOR);
            }
        }
        return postRepository.findPostsWithTags(builder, size);
    }

    // 게시글 생성
    public PostResDTO.CreatePost createPost(
//            User user,
            PostReqDTO.CreatePost dto
    ){
        // 게시글 저장
//        Post post = postRepository.save(PostConverter.toPost(dto, user));
        // 임시
        User user = userRepository.findUserById((1L));
        Post post = postRepository.save(PostConverter.toPost(dto, user));

        // 태그 생성 : 기존 태그 불러오기 + 없는 태그 저장하기
        List<Tag> foundTags = tagRepository.findByContentIn(dto.tags());
        Map<String, Tag> tagMap = foundTags.stream()
                .collect(Collectors.toMap(Tag::getContent, Function.identity()));

        List<Tag> tags = new ArrayList<>();
        for (String tagName : dto.tags()) {
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

        // 리턴
        LocalDateTime now = LocalDateTime.now();
        return PostConverter.toCreatePostDTO(post, now);
    }
}
