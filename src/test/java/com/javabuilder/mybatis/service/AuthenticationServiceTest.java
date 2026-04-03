package com.javabuilder.mybatis.service;

import com.javabuilder.mybatis.common.UserStatus;
import com.javabuilder.mybatis.dto.request.LoginRequest;
import com.javabuilder.mybatis.dto.response.TokenResponse;
import com.javabuilder.mybatis.model.User;
import com.javabuilder.mybatis.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private final String token = "eyJraWQiOiJRMFhidlJlXzQ.eyJzdWIiOiIzIiwiZXhwIjox.EqwOeWxYipCA_Qoy";

    private LoginRequest loginRequest;
    private TokenResponse tokenResponse;
    private User user;

    @BeforeEach
    void initData() {
        loginRequest = new LoginRequest("duc@gmail.com", "123456");
        tokenResponse = new TokenResponse(token);
        user = User.builder()
                .id(1L)
                .name("duc")
                .email("duc@gmail.com")
                .status(UserStatus.ACTIVE)
                .build();
    }

    @Test
    void login_whenValid_thenReturnToken() {
        Authentication authentication = mock(Authentication.class);
        UserPrincipal userPrincipal = new UserPrincipal(user);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(jwtService.getAuthorities(userPrincipal.getAuthorities())).thenReturn(List.of("STUDENT"));
        when(jwtService.generateToken(1L, List.of("STUDENT"))).thenReturn(token);

        TokenResponse result = authenticationService.authenticate(loginRequest);

        assertThat(result)
                .isNotNull()
                .isEqualTo(tokenResponse);
    }
}
