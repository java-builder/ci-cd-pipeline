package com.javabuilder.mybatis.dto.request;

public record LoginRequest(
        String email,
        String password
) {
}
