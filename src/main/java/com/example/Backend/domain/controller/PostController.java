package com.example.Backend.domain.controller;

import com.example.Backend.domain.dto.PostReqDTO;
import com.example.Backend.domain.dto.PostResDTO;
import com.example.Backend.domain.service.PostService;
import com.example.Backend.global.apiPayload.CustomResponse;
import com.example.Backend.global.auth.AuthUser;
import com.example.Backend.global.validation.annotation.TagValidation;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Validated
public class PostController {

    private final PostService postService;

    // GET
    // 게시글ID로 게시글 조회
    @Operation(
            summary = "게시글ID로 게시글 조회(단일) By 김주헌",
            description = "게시글 ID를 통해 게시글 내용을 불러옵니다."
    )
    @GetMapping("/posts/{postId}")
    public CustomResponse<PostResDTO.GetPostById> getPostById(
            @PathVariable Long postId
    ){
        return CustomResponse.ok(postService.getPostById(postId));
    }

    // 태그와 관련된 게시글 모두 조회
    @Operation(
            summary = "태그와 관련된 게시글 모두 조회 By 김주헌",
            description = "태그와 관련된 게시글을 모두 조회합니다." +
                    " 오프셋 페이지네이션으로, 검색할 태그들과 현재 페이지(오프셋), 불러올 게시글 수(페이지 사이즈)를 정해주세요."
    )
    @GetMapping("/posts")
    public CustomResponse<PostResDTO.PageablePost<PostResDTO.SimplePost>> getPostsWithTags(
            @RequestParam(required = false) @TagValidation @Valid @NotEmpty(message = "검색할 태그는 필수 입력입니다.")
            List<String> tags,
            @RequestParam(defaultValue = "-1") @NotNull(message = "커서의 기본값은 -1입니다.")
            @Min(value = -1, message = "커서는 -1 이상이어야 합니다.")
            String cursor,
            @RequestParam(defaultValue = "1") @NotNull(message = "조회할 데이터 사이즈를 요청해야 합니다.")
            @Min(value = 1, message = "게시글은 최소 하나 이상 조회해야 합니다.")
            int size
    ){
        return CustomResponse.ok(postService.getPostsWithTags(tags, cursor, size));
    }
    // POST
    // 게시글 생성
    @Operation(
            summary = "게시글 생성 By 김주헌",
            description = "게시글을 생성합니다."
    )
    @PostMapping("/post")
    public CustomResponse<PostResDTO.CreatePost> createPost(
            @RequestBody @Valid PostReqDTO.CreatePost dto,
            @AuthenticationPrincipal AuthUser user
    ){
        return CustomResponse.created(postService.createPost(user, dto));
    }

    // 게시글 좋아요
    @Operation(
            summary = "게시글 좋아요 By 김주헌",
            description = "게시글에 좋아요를 표기합니다."
    )
    @PostMapping("/post/{postId}/like")
    public CustomResponse<PostResDTO.LikePost> likePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal AuthUser user
    ){
        return CustomResponse.ok(postService.LikePost(user, postId));
    }
    // DELETE
}
