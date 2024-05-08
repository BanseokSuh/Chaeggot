package com.banny.chaggot.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostCreateResponse {
        private Long postId;

        public static PostCreateResponse of(Long postId) {
                return new PostCreateResponse(postId);
        }
}
