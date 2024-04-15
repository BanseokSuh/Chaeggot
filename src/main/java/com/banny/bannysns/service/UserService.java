package com.banny.bannysns.service;

import com.banny.bannysns.exception.ApplicationException;
import com.banny.bannysns.exception.ErrorCode;
import com.banny.bannysns.model.User;
import com.banny.bannysns.model.entity.UserEntity;
import com.banny.bannysns.repository.UserCacheRepository;
import com.banny.bannysns.repository.UserEntityRepository;
import com.banny.bannysns.util.JwtTokenUtils;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserEntityRepository userEntityRepository;
    private final UserCacheRepository userCacheRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.access-expired-time-ms}")
    private Long accessExpiredTimeMs;

    /**
     * 회원가입
     * @param userId
     * @param userName
     * @param password
     * @return User
     */
    @Transactional
    public User join(String userId, String userName, String password) {
        validateUserInfo(userId, userName, password);

        userEntityRepository.findByUserId(userId).ifPresent(it -> {
            throw new ApplicationException(ErrorCode.DUPLICATED_USER_ID, String.format("User ID %s is duplicated", userId));
        });

        UserEntity userEntity = userEntityRepository.save(UserEntity.of(userId, userName, encoder.encode(password)));
        return User.fromEntity(userEntity);
    }

    /**
     * 로그인
     * @param userId
     * @param password
     * @return token
     */
    public String login(String userId, String password) {
        User user = loadUserByUserId(userId);

        if (!encoder.matches(password, user.getPassword())) {
            throw new ApplicationException(ErrorCode.WRONG_PASSWORD, "Password is wrong");
        }

        String accessToken = JwtTokenUtils.generateToken(user, secretKey, accessExpiredTimeMs);

        // Cache user
        userCacheRepository.setUser(user);

        return accessToken;
    }


    /**
     * userId로 유저 정보 조회
     * @param userId
     * @return User
     */
    public User loadUserByUserId(String userId) {
        return userCacheRepository.getUser(userId).orElseGet(() ->
                userEntityRepository.findByUserId(userId).map(User::fromEntity).orElseThrow(() ->
                        new ApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", userId)))
        );
    }

    /**
     * 유저 정보 유효성 검사
     * @param userId
     * @param userName
     * @param password
     */
    private void validateUserInfo(String userId, String userName, String password) {
        if (StringUtils.isBlank(userId)) {
            throw new ApplicationException(ErrorCode.EMPTY_USER_ID, String.format("User ID %s should not be empty", userId));
        }

        if (userId.length() < 5) {
            throw new ApplicationException(ErrorCode.INVALID_USER_ID, String.format("User ID %s is too short", userId));
        }

        if (StringUtils.isBlank(userName)) {
            throw new ApplicationException(ErrorCode.EMPTY_USER_NAME, String.format("Username %s should not be empty", userName));
        }

        if (StringUtils.isBlank(password)) {
            throw new ApplicationException(ErrorCode.EMPTY_PASSWORD, String.format("Password %s should not be empty", password));
        }

        if (password.length() < 5) {
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD, String.format("Password %s is invalid", password));
        }

        Pattern pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.find()) {
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD, String.format("Password %s should include at least 1 special symbol", password));
        }
    }
}
