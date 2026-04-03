package com.javabuilder.mybatis.exception;

import lombok.Builder;

@Builder
public record ErrorResponse(
        int status,
        String message,
        String error,
        String path,
        long timestamp
) {
}
