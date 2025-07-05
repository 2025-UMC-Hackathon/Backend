package com.example.Backend.domain.repository;

import com.example.Backend.domain.entity.PostType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTypeRepository extends JpaRepository<PostType, Long> {
}
