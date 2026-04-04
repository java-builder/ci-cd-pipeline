package com.javabuilder.mybatis.service;

import com.javabuilder.mybatis.dto.request.LoginRequest;
import com.javabuilder.mybatis.dto.response.TokenResponse;
import com.javabuilder.mybatis.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "AUTHENTICATION-SERVICE")
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public TokenResponse authenticate(LoginRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        log.info("Login success with email: {}", request.email());

        UserPrincipal userPrincipal = (UserPrincipal) authenticate.getPrincipal();

        List<String> authorities = jwtService.getAuthorities(userPrincipal.getAuthorities());

        String token = jwtService.generateToken(userPrincipal.getId(), authorities);

        return TokenResponse.builder()
                .token(token)
                .build();
    }
}
