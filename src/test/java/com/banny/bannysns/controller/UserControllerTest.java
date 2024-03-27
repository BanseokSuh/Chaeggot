package com.banny.bannysns.controller;

import com.banny.bannysns.controller.request.UserJoinRequest;
import com.banny.bannysns.model.User;
import com.banny.bannysns.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    public void join() throws Exception {
        // given
        String userId = "testUser002";
        String userName = "서반석";
        String password = "testUser!";

        // mocking
        when(mock(UserService.class).join(userId, userName, password)).thenReturn(mock(User.class));

        mockMvc.perform(post("/api/v1/user/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userId, userName, password)))
        ).andDo(print()).andExpect(status().isOk());
    }



    // 회원가입 시 userId를 입력하지 않으면 에러를 반환한다
    // 회원가입 시 userId가 5자리 미만인 경우 에러를 반환한다

    // 회원가입 시 userName을 입력하지 않으면 에러를 반환한다
    // 회원가입 시 비밀번호를 입력하지 않으면 에러를 반환한다

    // 회원가입 시 비밀번호가 5자리 미만인 경우 에러를 반환한다
    // 회원가입 시 비밀번호에 특수문자가 없는 경우 에러를 반환한다

    // 회원가입 시 이미 회원가입된 userId로 회원가입을 하는 경우 에러를 반환한다
}