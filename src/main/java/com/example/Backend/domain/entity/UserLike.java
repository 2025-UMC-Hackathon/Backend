package com.example.Backend.domain.entity;


import com.example.Backend.domain.enums.PostLike;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_like")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_like")
    @Enumerated(EnumType.STRING)
    private PostLike isLike;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    // update
    public void updateLike(PostLike postLike){
        this.isLike = postLike;
    }
}
