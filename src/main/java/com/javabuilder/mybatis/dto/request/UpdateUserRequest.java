package com.javabuilder.mybatis.dto.request;

public record UpdateUserRequest(
        String name,
        String email
) {
}
