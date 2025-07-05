//package com.example.Backend.domain.controller;
//
//import com.example.Backend.domain.dto.CommentRequestDto;
//import com.example.Backend.domain.dto.CommentResponseDto;
//import com.example.Backend.domain.service.CommentService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/comments")
//@RequiredArgsConstructor
//public class CommentController {
//
//    private final CommentService commentService;
//
//    // 댓글 업로드
//    @PostMapping
//    public ResponseEntity<Void> createComment(@RequestBody CommentRequestDto requestDto) {
//        commentService.uploadComment(requestDto);
//        return ResponseEntity.ok().build();
//    }
//
//    // 특정 게시글의 댓글 조회
//    @GetMapping("/{postId}")
//    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable Long postId) {
//        return ResponseEntity.ok(commentService.getCommentsByPost(postId));
//    }
//}
