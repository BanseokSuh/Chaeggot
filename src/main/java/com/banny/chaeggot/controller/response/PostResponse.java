package com.banny.chaeggot.controller.response;

import com.banny.chaeggot.model.Post;
import com.banny.chaeggot.model.User;
import com.banny.chaeggot.model.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Time;
import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private UserResponse user;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                UserResponse.from(post.getUser()),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
