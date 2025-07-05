package com.example.Backend.domain.repository;

import com.example.Backend.domain.entity.Comment;
import com.example.Backend.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 특정 게시글(post) 기준 댓글 조회
    List<Comment> findByPost(Post post);
}

