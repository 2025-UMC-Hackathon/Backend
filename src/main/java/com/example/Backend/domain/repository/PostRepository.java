package com.example.Backend.domain.repository;

import com.example.Backend.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer>, PostQueryDsl {
    Post findPostById(Long id);
}
