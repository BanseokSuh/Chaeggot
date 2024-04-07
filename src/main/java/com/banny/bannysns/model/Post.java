package com.banny.bannysns.model;

import com.banny.bannysns.model.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    private Long id;
    private String title;
    private String content;
    private User user;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static Post fromEntity(PostEntity entity) {
        Post post = new Post();
        post.setId(entity.getId());
        post.setTitle(entity.getTitle());
        post.setContent(entity.getContent());
        post.setUser(User.fromEntity(entity.getUser()));
        post.setCreatedAt(entity.getCreatedAt());
        post.setUpdatedAt(entity.getUpdatedAt());
        post.setDeletedAt(entity.getDeletedAt());

        return post;
    }
}
