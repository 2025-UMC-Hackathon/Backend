package com.example.Backend.domain.converter;

import com.example.Backend.domain.entity.Post;
import com.example.Backend.domain.entity.PostTag;
import com.example.Backend.domain.entity.Tag;

public class PostTagConverter {

    // Post, Tag -> PostTag
    public static PostTag toPostTag(
            Post post,
            Tag tag
    ){
        return PostTag.builder()
                .post(post)
                .tag(tag)
                .build();
    }
}
