package com.example.Backend.domain.repository;

import com.example.Backend.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer>, PostQueryDsl {
    Optional<Post> findPostById(Long id);
}
