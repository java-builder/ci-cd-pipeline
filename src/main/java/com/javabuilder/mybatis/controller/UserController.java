package com.javabuilder.mybatis.controller;

import com.javabuilder.mybatis.dto.request.CreateUserRequest;
import com.javabuilder.mybatis.dto.request.UpdateUserRequest;
import com.javabuilder.mybatis.dto.response.CreateUserResponse;
import com.javabuilder.mybatis.dto.response.PageResponse;
import com.javabuilder.mybatis.dto.response.UserResponse;
import com.javabuilder.mybatis.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @GetMapping
    public ResponseEntity<PageResponse<UserResponse>> searchByEmail(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "") String email
    ) {
        return ResponseEntity.ok(userService.findByEmail(page, size, email));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public void updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        userService.update(id, request);
    }

    @GetMapping("/my-info")
    public ResponseEntity<UserResponse> myInfo(@AuthenticationPrincipal Jwt jwt) {
        var userId = Long.parseLong(jwt.getSubject());
        return ResponseEntity.ok(userService.myInfo(userId));
    }
}
