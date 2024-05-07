package com.banny.springboot.controller;

import com.banny.springboot.controller.request.UserJoinRequest;
import com.banny.springboot.controller.request.UserLoginRequest;
import com.banny.springboot.exception.ApplicationException;
import com.banny.springboot.exception.ErrorCode;
import com.banny.springboot.model.User;
import com.banny.springboot.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("회원가입")
    @Transactional
    public void join() throws Exception {
        // given
        String userId = "testUser000";
        String userName = "서반석";
        String password = "testUser!";

        // expected
        mockMvc.perform(post("/api/v1/user/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userId, userName, password))))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 시 userId를 입력하지 않으면 에러를 반환한다")
    public void joinWithoutUserId() throws Exception {
        // given
        String userId = ""; // Empty userId
        String userName = "서반석";
        String password = "testUser!";

        // expected
        mockMvc.perform(post("/api/v1/user/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userId, userName, password))))
                .andExpect(jsonPath("$.code").value(ErrorCode.EMPTY_USER_ID.getCode()))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 시 userId가 5자리 미만인 경우 에러를 반환한다")
    public void joinWithUserIdWithNotEnoughLength() throws Exception {
        // given
        String userId = "test"; // 4 letters of userId
        String userName = "서반석";
        String password = "testUser!";

        // expected
        mockMvc.perform(post("/api/v1/user/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userId, userName, password))))
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_USER_ID.getCode()))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 시 이미 회원가입된 userId로 회원가입을 하는 경우 에러를 반환한다")
    public void joinWithDuplicatedUserId() throws Exception {
        // given
        String userId = "admin00"; // Duplicated userId
        String userName = "서반석";
        String password = "testUser!";

        // expected
        mockMvc.perform(post("/api/v1/user/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userId, userName, password))))
                .andExpect(jsonPath("$.code").value(ErrorCode.DUPLICATED_USER_ID.getCode()))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 시 userName을 입력하지 않으면 에러를 반환한다")
    public void joinWithoutUserName() throws Exception {
        // given
        String userId = "testUser000";
        String userName = ""; // Empty userName
        String password = "testUser!";

        // expected
        mockMvc.perform(post("/api/v1/user/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userId, userName, password))))
                .andExpect(jsonPath("$.code").value(ErrorCode.EMPTY_USER_NAME.getCode()))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 시 비밀번호를 입력하지 않으면 에러를 반환한다")
    void joinWithoutPassword() throws Exception {
        // given
        String userId = "testUser000";
        String userName = "서반석";
        String password = ""; // Empty password

        // expected
        mockMvc.perform(post("/api/v1/user/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userId, userName, password))))
                .andExpect(jsonPath("$.code").value(ErrorCode.EMPTY_PASSWORD.getCode()))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 시 비밀번호가 5자리 미만인 경우 에러를 반환한다")
    void joinWithPasswordWithNotEnoughLength() throws Exception {
        // given
        String userId = "testUser000";
        String userName = "서반석";
        String password = "test"; // 4 letters of password

        // expected
        mockMvc.perform(post("/api/v1/user/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userId, userName, password))))
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_PASSWORD.getCode()))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 시 비밀번호에 특수문자가 없는 경우 에러를 반환한다")
    void joinWithPasswordWithoutSpecialSymbol() throws Exception {
        // given
        String userId = "testUser000";
        String userName = "서반석";
        String password = "testUser"; // No special character in password

        // expected
        mockMvc.perform(post("/api/v1/user/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userId, userName, password))))
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_PASSWORD.getCode()))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인")
    public void login() throws Exception {
        // given
        String userId = "admin00";
        String password = "admin00!";

        // expected
        mockMvc.perform(post("/api/v1/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userId, password))))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 시 회원가입이 안 된 userId를 입력할 경우 에러를 반환한다")
    public void loginWithNotJoinedUserId() throws Exception {
        // given
        String userId = "notJoinedUser";
        String password = "notJoinedUser!";

        // expected
        mockMvc.perform(post("/api/v1/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userId, password))))
                .andExpect(jsonPath("$.code").value(ErrorCode.USER_NOT_FOUND.getCode()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 시 틀린 password를 입력할 경우 에러를 반환한다")
    public void loginWithWrongPassword() throws Exception {
        // given
        String userId = "admin00";
        String password = "wrongPassword";

        // expected
        mockMvc.perform(post("/api/v1/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userId, password))))
                .andExpect(jsonPath("$.code").value(ErrorCode.WRONG_PASSWORD.getCode()))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}