package com.example.Backend.domain.controller;

import com.example.Backend.domain.dto.CommentRequestDto;
import com.example.Backend.domain.dto.CommentResponseDto;
import com.example.Backend.domain.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @Operation(summary = "댓글 등록", description = "일반 댓글 또는 대댓글을 저장합니다.")
    public ResponseEntity<String> uploadComment(@RequestBody CommentRequestDto requestDto) {
        commentService.uploadComment(requestDto);
        return ResponseEntity.ok("댓글이 등록되었습니다.");
    }

    @GetMapping("/{postId}")
    @Operation(summary = "댓글 조회", description = "특정 게시글의 댓글과 대댓글을 트리 구조로 조회합니다.")
    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPost(postId));
    }

    @Operation(summary = "댓글 삭제", description = "댓글 ID를 기반으로 댓글을 삭제합니다.")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }

}
