package com.example.Backend.domain.converter;

import com.example.Backend.domain.entity.Post;
import com.example.Backend.domain.entity.User;
import com.example.Backend.domain.entity.UserLike;
import com.example.Backend.domain.enums.PostLike;

public class UserLikeConverter {

    // userId, postId -> userLike
    public static UserLike toUserLike(
            User user,
            Post post,
            PostLike isLike
    ) {
        return UserLike.builder()
                .user(user)
                .post(post)
                .isLike(isLike)
                .build();
    }
}
