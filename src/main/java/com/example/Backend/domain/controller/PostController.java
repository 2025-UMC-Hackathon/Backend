package com.example.Backend.domain.controller;

import com.example.Backend.domain.dto.request.PostReqDTO;
import com.example.Backend.domain.dto.response.PostResDTO;
import com.example.Backend.domain.service.PostService;
import com.example.Backend.global.apiPayload.CustomResponse;
import com.example.Backend.global.auth.AuthUser;
import com.example.Backend.global.validation.annotation.TagValidation;
import com.example.Backend.global.validation.annotation.TypeValid;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
                    " 커서기반 페이지네이션으로, tags => 고민 관련 태그, types => 장애유형 관련 태그, 커서, 불러올 게시글 수(페이지 사이즈)를 정해주세요."
    )
    @GetMapping("/posts")
    public CustomResponse<PostResDTO.PageablePost<PostResDTO.SimplePost>> getPostsWithTags(
            @RequestParam(required = false) @TagValidation @Valid
            List<String> tags,
            @RequestParam(required = false) @TypeValid @Valid
            List<String> types,
            @RequestParam(defaultValue = "-1") @NotNull(message = "커서의 기본값은 -1입니다.")
            @Min(value = -1, message = "커서는 -1 이상이어야 합니다.")
            String cursor,
            @RequestParam(defaultValue = "1") @NotNull(message = "조회할 데이터 사이즈를 요청해야 합니다.")
            @Min(value = 1, message = "게시글은 최소 하나 이상 조회해야 합니다.")
            int size
    ){
        return CustomResponse.ok(postService.getPostsWithTags(tags, types, cursor, size));
    }

    // 내가 좋아요 누른 게시글 조회
    @GetMapping("/me/like-post")
    @Operation(
            summary = "내가 좋아요 누른 게시글 조회 by 김주헌",
            description = "마이페이지에서 좋아요를 누른 게시글을 조회합니다. " +
                    "커서 기반 페이지네이션, 최신 순으로 정렬합니다."
    )
    public CustomResponse<PostResDTO.PageablePost<PostResDTO.FullPost>> getLikedPosts(
            @AuthenticationPrincipal
            AuthUser user,
            @RequestParam(defaultValue = "-1") @NotNull(message = "커서의 기본값은 -1입니다.")
            @Min(value = -1, message = "커서는 -1 이상이어야 합니다.")
            String cursor,
            @RequestParam(defaultValue = "1") @NotNull(message = "조회할 데이터 사이즈를 요청해야 합니다.")
            @Min(value = 1, message = "게시글은 최소 하나 이상 조회해야 합니다.")
            int size
    ) {

        return CustomResponse.ok(postService.getMyLikePost(user, cursor, size));
    }

    // 내가 작성한 게시글 조회
    @GetMapping("/me/posts")
    @Operation(
            summary = "내가 작성한 게시글 조회 (마이페이지) by 김주헌",
            description = "마이페이지에서 내가 올렸던 게시글을 조회합니다. " +
                    "커서 기반 페이지네이션, 최신 순으로 정렬합니다."
    )
    public CustomResponse<PostResDTO.PageablePost<PostResDTO.FullPost>> getMyPosts(
            @AuthenticationPrincipal
            AuthUser user,
            @RequestParam(defaultValue = "-1") @NotNull(message = "커서의 기본값은 -1입니다.")
            @Min(value = -1, message = "커서는 -1 이상이어야 합니다.")
            String cursor,
            @RequestParam(defaultValue = "1") @NotNull(message = "조회할 데이터 사이즈를 요청해야 합니다.")
            @Min(value = 1, message = "게시글은 최소 하나 이상 조회해야 합니다.")
            int size
    ) {
        return CustomResponse.ok(postService.getMyPosts(user, cursor, size));
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
            description = "게시글에 좋아요를 표기합니다. 재호출을 통해 좋아요 -> 좋아요 취소 할 수 있습니다."
    )
    @PostMapping("/posts/{postId}/like")
    public CustomResponse<PostResDTO.LikePost> likePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal AuthUser user
    ){
        return CustomResponse.ok(postService.LikePost(user, postId));
    }
    // DELETE
    // 게시글 삭제
    @Operation(
            summary = "게시글 삭제 By 김주헌",
            description = "게시글을 삭제합니다. (Hard Delete)"
    )
    @DeleteMapping("/posts/{postId}")
    public CustomResponse<PostResDTO.DeletePost> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal AuthUser user
    ){
        return CustomResponse.ok(postService.deletePost(postId, user));
    }

}
