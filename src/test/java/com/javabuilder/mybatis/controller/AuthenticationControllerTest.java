package com.javabuilder.mybatis.controller;

import com.javabuilder.mybatis.dto.request.LoginRequest;
import com.javabuilder.mybatis.dto.response.TokenResponse;
import com.javabuilder.mybatis.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.json.JsonMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test.properties")
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @MockitoBean
    private AuthenticationService authenticationService;

    private LoginRequest loginRequest;
    private TokenResponse tokenResponse;
    private String token = "eyJraWQiOiJRMFhidlJlXzQ.eyJzdWIiOiIzIiwiZXhwIjox.EqwOeWxYipCA_Qoy";

    @BeforeEach
    void initData() {
        loginRequest = new LoginRequest("duc@gmail.com", "123456");
        tokenResponse = new TokenResponse(token);
    }

    @Test
    void login_whenValidRequest_thenReturnToken() throws Exception {
        // GIVE
        when(authenticationService.authenticate(any())).thenReturn(tokenResponse);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonMapper.writeValueAsString(loginRequest))
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(jsonMapper.writeValueAsString(tokenResponse)));
    }
}
