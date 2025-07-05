package com.example.Backend.domain.repository;

import com.example.Backend.domain.entity.UserLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserLikeRepository extends JpaRepository<UserLike, Long> {
    Optional<UserLike> findByUserIdAndPostId(Long userId, Long postId);
}
