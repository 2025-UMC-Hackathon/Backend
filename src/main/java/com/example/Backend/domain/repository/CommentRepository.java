package com.example.Backend.domain.repository;

import com.example.Backend.domain.entity.Comment;
import com.example.Backend.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 특정 게시글의 댓글 전체 조회
    List<Comment> findByPostId(Long postId);
}
