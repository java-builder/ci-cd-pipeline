package com.javabuilder.mybatis.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(500)
                .message(e.getMessage())
                .error("Internal Server Error")
                .path(request.getDescription(false))
                .timestamp(System.currentTimeMillis())
                .build();

        return ResponseEntity.status(500).body(errorResponse);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponse> disabledException(WebRequest request) {
        ErrorCode errorCode = ErrorCode.ACCOUNT_DISABLED;

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(errorCode.getCode())
                .message(errorCode.getMessage())
                .error(errorCode.getStatus().getReasonPhrase())
                .path(request.getDescription(false).replace("uri=", ""))
                .timestamp(System.currentTimeMillis())
                .build();

        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handlerApplicationException(ApplicationException exception, WebRequest request) {
        ErrorCode errorCode = exception.getErrorCode();

        ErrorResponse errorResponse = ErrorResponse.builder()
                 .status(errorCode.getCode())
                 .message(errorCode.getMessage())
                 .error(errorCode.getStatus().getReasonPhrase())
                 .path(request.getDescription(false).replace("uri=", ""))
                 .timestamp(System.currentTimeMillis())
                 .build();

        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e, WebRequest request) {

        BindingResult bindingResult = e.getBindingResult();

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        List<String> errors =
                fieldErrors.stream().map(FieldError::getDefaultMessage).toList();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(errors.size() > 1 ? String.valueOf(errors) : errors.getFirst())
                .path(request.getDescription(false).replace("uri=", ""))
                .timestamp(System.currentTimeMillis())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}
