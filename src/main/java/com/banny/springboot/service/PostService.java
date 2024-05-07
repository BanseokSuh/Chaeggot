package com.banny.springboot.service;

import com.banny.springboot.exception.ApplicationException;
import com.banny.springboot.exception.ErrorCode;
import com.banny.springboot.model.entity.PostEntity;
import com.banny.springboot.model.entity.UserEntity;
import com.banny.springboot.repository.PostEntityRepository;
import com.banny.springboot.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;

    /**
     * Create post
     *
     * @param title
     * @param content
     * @return
     */
    public Long createPost(String title, String content, String userId) {
        UserEntity userEntity = getUserEntityOrException(userId);

        PostEntity postEntity = postEntityRepository.save(PostEntity.of(title, content, userEntity));
        return postEntity.getPostIdx();
    }

    /**
     * Modify post
     * @param title
     * @param content
     * @param userId
     * @param postIdx
     */
    public void modifyPost(String title, String content, String userId, Long postIdx) {
        UserEntity userEntity = getUserEntityOrException(userId);
        PostEntity postEntity = getPostEntityOrException(postIdx);

        if (!postEntity.getUser().equals(userEntity)) {
            throw new ApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", userId, postIdx));
        }

        postEntity.modify(title, content);

        postEntityRepository.saveAndFlush(postEntity);
    }

    // =================================================================================================================

    /**
     * Get user entity or exception
     * @param userId
     * @return
     */
    public UserEntity getUserEntityOrException(String userId) {
        return userEntityRepository.findByUserId(userId).orElseThrow(() ->
                new ApplicationException(ErrorCode.USER_NOT_FOUND, String.format("User ID %s not found", userId)));
    }

    /**
     * Get post entity or exception
     * @param postIdx
     * @return
     */
    private PostEntity getPostEntityOrException(Long postIdx) {
        return postEntityRepository.findById(postIdx).orElseThrow(() ->
                new ApplicationException(ErrorCode.POST_NOT_FOUND, String.format("PostIdx %s not found", postIdx)));
    }
}
