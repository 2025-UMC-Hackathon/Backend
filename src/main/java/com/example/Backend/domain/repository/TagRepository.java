package com.example.Backend.domain.repository;

import com.example.Backend.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByContentIn(Collection<String> contents);
}
