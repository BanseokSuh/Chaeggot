package com.banny.chaeggot.controller.response;

import com.banny.chaeggot.model.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostCreateResponse {
    private Long id;

    public static PostCreateResponse from(Post post) {
        return new PostCreateResponse(post.getId());
    }
}
