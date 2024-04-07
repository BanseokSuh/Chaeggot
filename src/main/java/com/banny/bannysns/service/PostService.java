package com.banny.bannysns.service;

import com.banny.bannysns.exception.ApplicationException;
import com.banny.bannysns.exception.ErrorCode;
import com.banny.bannysns.model.entity.PostEntity;
import com.banny.bannysns.model.entity.UserEntity;
import com.banny.bannysns.repository.PostEntityRepository;
import com.banny.bannysns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;

    public Long createPost(String title, String content) {
        String userId = "admin06"; // TODO: 추후 인증 구현 후 수정
        UserEntity userEntity = getUserEntityOrException(userId);

        PostEntity postEntity = postEntityRepository.save(PostEntity.of(title, content, userEntity));
        return postEntity.getId();
    }

    public UserEntity getUserEntityOrException(String userId) {
        return userEntityRepository.findByUserId(userId).orElseThrow(() ->
                new ApplicationException(ErrorCode.USER_NOT_FOUND, String.format("User ID %s not found", userId)));
    }
}
