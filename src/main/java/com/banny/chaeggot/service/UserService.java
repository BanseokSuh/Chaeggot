package com.banny.chaeggot.service;

import com.banny.chaeggot.exception.ApplicationException;
import com.banny.chaeggot.exception.ErrorCode;
import com.banny.chaeggot.model.User;
import com.banny.chaeggot.model.entity.UserEntity;
import com.banny.chaeggot.repository.UserCacheRepository;
import com.banny.chaeggot.repository.UserEntityRepository;
import com.banny.chaeggot.util.JwtTokenUtils;
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
    private Long accessTokenExpiredTimeMs;

    /**
     * Join
     *
     * @param loginId
     * @param userName
     * @param password
     * @return User
     */
    @Transactional
    public User join(String loginId, String userName, String password) {

        // Valid check for user info
        validateUserInfo(loginId, userName, password);

        // Check duplicated user id
        userEntityRepository.findByLoginId(loginId).ifPresent(it -> {
            throw new ApplicationException(ErrorCode.DUPLICATED_USER_ID, String.format("User ID %s is duplicated", loginId));
        });

        // Save user info
        UserEntity userEntity = userEntityRepository.save(UserEntity.of(loginId, userName, encoder.encode(password)));

        return User.fromEntity(userEntity);
    }

    /**
     * Login
     *
     * @param loginId
     * @param password
     * @return token
     */
    public String login(String loginId, String password) {

        // Get user info
        User user = loadUserByLoginId(loginId);

        // Check password if it is correct
        if (!encoder.matches(password, user.getPassword())) {
            throw new ApplicationException(ErrorCode.WRONG_PASSWORD, "Password is wrong");
        }

        // Generate token with secret key and expired time
        String accessToken = JwtTokenUtils.generateToken(user, secretKey, accessTokenExpiredTimeMs);

        // Cache user info
        userCacheRepository.setUser(user);

        return accessToken;
    }

    // =================================================================================================================

    /**
     * Load user by loginId.
     * - Get user from cache first.
     * - If not exist, get user from DB using .orElseGet() method.
     * - If not exist, throw exception.
     *
     * @param loginId
     * @return User
     */
    public User loadUserByLoginId(String loginId) {
        return userCacheRepository.getUser(loginId).orElseGet(() ->
                userEntityRepository.findByLoginId(loginId).map(User::fromEntity).orElseThrow(() ->
                        new ApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", loginId)))
        );
    }

    /**
     * Load UserEntity by id
     *
     * @param id
     * @return
     */
    public UserEntity getUserEntityOrException(Long id) {
        return userEntityRepository.findById(id).orElseThrow(() ->
                new ApplicationException(ErrorCode.USER_NOT_FOUND, String.format("UserIdx[%s] not found", id)));
    }

    /**
     * Valid check for user info
     * todo: Move this method to a separate class
     *
     * @param loginId
     * @param userName
     * @param password
     */
    private void validateUserInfo(String loginId, String userName, String password) {
        if (StringUtils.isBlank(loginId)) {
            throw new ApplicationException(ErrorCode.EMPTY_USER_ID, String.format("User ID %s should not be empty", loginId));
        }

        if (loginId.length() < 5) {
            throw new ApplicationException(ErrorCode.INVALID_USER_ID, String.format("User ID %s is too short", loginId));
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
