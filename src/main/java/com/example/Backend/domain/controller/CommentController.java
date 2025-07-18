package com.example.Backend.domain.controller;

import com.example.Backend.domain.dto.response.CommentResponseDto;
import com.example.Backend.domain.service.CommentService;
import com.example.Backend.global.apiPayload.CustomResponse;
import com.example.Backend.global.auth.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    @Operation(summary = "댓글 등록", description = "일반 댓글 또는 대댓글을 저장합니다.")
    public CustomResponse<String> uploadComment(
            @RequestParam Long postId,
            @RequestParam(required = false) Long parentId,
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam String content
    ) {

        commentService.uploadComment(postId, parentId , authUser , content);
        return CustomResponse.ok("댓글이 등록되었습니다.");
    }

    @GetMapping("posts/{postId}/comments")
    @Operation(summary = "댓글 조회", description = "특정 게시글의 댓글과 대댓글을 트리 구조로 조회합니다.")
    public CustomResponse<List<CommentResponseDto>> getComments(@PathVariable Long postId) {
        return CustomResponse.ok(commentService.getCommentsByPost(postId));
    }

    @Operation(summary = "댓글 삭제", description = "댓글 ID를 기반으로 댓글을 삭제합니다.")
    @DeleteMapping("/comments/{commentId}")
    public CustomResponse<String> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return CustomResponse.ok("댓글이 삭제되었습니다.");
    }

}
