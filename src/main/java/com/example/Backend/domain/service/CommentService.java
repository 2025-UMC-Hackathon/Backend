package com.example.Backend.domain.service;

import com.example.Backend.domain.dto.response.CommentResponseDto;
import com.example.Backend.domain.entity.Comment;
import com.example.Backend.domain.entity.Post;
import com.example.Backend.domain.entity.User;
import com.example.Backend.domain.exception.CommentException;
import com.example.Backend.domain.exception.code.CommentErrorCode;
import com.example.Backend.domain.repository.CommentRepository;
import com.example.Backend.domain.repository.PostRepository;
import com.example.Backend.domain.repository.UserRepository;
import com.example.Backend.global.auth.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.Backend.domain.exception.code.CommentErrorCode.PARENT_COMMENT_NOT_FOUND;
import static com.example.Backend.domain.exception.code.PostErrorCode.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void uploadComment(
            Long postId,
            Long parentId,
            AuthUser authUser,
            String content
    ) {

        // 유저 정보 조회
        User user = userRepository.findById(authUser.getUserId()).orElseThrow(() ->
                new CommentException(CommentErrorCode.USER_NOT_FOUND));

        if (content == null) {
            throw new CommentException(CommentErrorCode.EMPTY_CONTENT);
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommentException(NOT_FOUND));

        Comment parent = null;
        if (parentId != null) {
            parent = commentRepository.findById(parentId)
                    .orElseThrow(() -> new CommentException(PARENT_COMMENT_NOT_FOUND));
            //부모 댓글의 postId와 현재 댓글의 postId가 다른 경우 예외 처리
            if (!parent.getPost().getId().equals(post.getId())) {
                throw new CommentException(CommentErrorCode.PARENT_POST_NOT_MATCH);
            }
        }

        //converter!!
        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(content)
                .parent(parent)
                .build();

        commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByPost(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);


        if (comments.isEmpty()) {
            throw new CommentException(CommentErrorCode.COMMENTS_EMPTY);
        }

        // DTO 변환
        List<CommentResponseDto> commentDtos = comments.stream()
                .map(this::toDto)
                .toList();

        // 대댓글 구조화
        Map<Long, CommentResponseDto> map = new HashMap<>();
        List<CommentResponseDto> rootComments = new ArrayList<>();

        for (CommentResponseDto dto : commentDtos) {
            map.put(dto.getId(), dto);
        }

        for (CommentResponseDto dto : commentDtos) {
            if (dto.getParentId() == null) {
                rootComments.add(dto);
            } else {
                CommentResponseDto parent = map.get(dto.getParentId());
                if (parent != null) {
                    parent.getChildren().add(dto);
                }
            }
        }

        return rootComments;
    }

    private CommentResponseDto toDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getNickname(),
                comment.getUser().getId(),
                comment.getPost().getId(),
                comment.getParent() != null ? comment.getParent().getId() : null,
                new ArrayList<>(),  // children 초기화
                toRelativeTime(comment.getCreatedAt())  // ✅ 작성 시간 계산
        );
    }

    private String toRelativeTime(LocalDateTime time) {
        Duration duration = Duration.between(time, LocalDateTime.now());

        if (duration.toMinutes() < 1) return "방금 전";
        if (duration.toMinutes() < 60) return duration.toMinutes() + "분 전";
        if (duration.toHours() < 24) return duration.toHours() + "시간 전";
        if (duration.toDays() < 7) return duration.toDays() + "일 전";
        if (duration.toDays() < 30) return (duration.toDays() / 7) + "주 전";
        if (duration.toDays() < 365) return (duration.toDays() / 30) + "개월 전";
        return (duration.toDays() / 365) + "년 전";
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));
        commentRepository.delete(comment);
    }

}
