package com.example.Backend.domain.converter;

import com.example.Backend.domain.entity.Tag;

public class TagConverter {

    // content -> Tag
    public static Tag toTag(
            String content
    ){
        return Tag.builder()
                .content(content)
                .build();
    }
}
