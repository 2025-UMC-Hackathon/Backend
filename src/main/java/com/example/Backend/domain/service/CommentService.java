//package com.example.Backend.domain.service;
//
//import com.example.Backend.domain.dto.CommentRequestDto;
//import com.example.Backend.domain.dto.CommentResponseDto;
//import com.example.Backend.domain.entity.Comment;
//import com.example.Backend.domain.entity.Post;
//import com.example.Backend.domain.entity.User;
//import com.example.Backend.domain.repository.CommentRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.stream.Collectors;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class CommentService {
//
//    private final CommentRepository commentRepository;
//    private final PostRepository postRepository;
//    private final UserRepository userRepository;
//
//    @Transactional
//    public void uploadComment(CommentRequestDto request) {
//        Post post = postRepository.findById(request.getPostId())
//                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
//
//        User user = userRepository.findById(request.getUserId())
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
//
//        Comment comment = Comment.builder()
//                .content(request.getContent())
//                .post(post)
//                .user(user)
//                .build();
//
//        commentRepository.save(comment);
//    }
//
//    @Transactional(readOnly = true)
//    public List<CommentResponseDto> getCommentsByPost(Long postId) {
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
//
//        return commentRepository.findByPost(post).stream()
//                .map(comment -> new CommentResponseDto(
//                        comment.getId(),
//                        comment.getContent(),
//                        comment.getUser().getNickname()  // 닉네임이 있다고 가정
//                ))
//                .collect(Collectors.toList());
//    }
//}
//
