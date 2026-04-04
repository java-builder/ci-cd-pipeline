package com.javabuilder.mybatis.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.javabuilder.mybatis.common.RoleName;
import com.javabuilder.mybatis.converter.UserConverter;
import com.javabuilder.mybatis.dto.request.CreateUserRequest;
import com.javabuilder.mybatis.dto.request.UpdateUserRequest;
import com.javabuilder.mybatis.dto.response.CreateUserResponse;
import com.javabuilder.mybatis.dto.response.PageResponse;
import com.javabuilder.mybatis.dto.response.UserResponse;
import com.javabuilder.mybatis.exception.ApplicationException;
import com.javabuilder.mybatis.exception.ErrorCode;
import com.javabuilder.mybatis.mapper.UserMapper;
import com.javabuilder.mybatis.model.Role;
import com.javabuilder.mybatis.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserMapper userMapper;
    private final RoleService roleService;
    private final UserRoleService userRoleService;
    private final UserConverter userConverter;

    @Transactional(rollbackFor = Exception.class)
    public CreateUserResponse createUser(CreateUserRequest request) {
        if(userMapper.existsByEmail(request.email()))
            throw new ApplicationException(ErrorCode.USER_READY_EXISTS);

        User user = userConverter.toUser(request);

        Role role = roleService.findOrCreateRole(RoleName.STUDENT.name());

        user.addRole(role);
        userMapper.insert(user);
        userRoleService.addRoleToUser(user.getId(), role.getId());

        log.info("User created with email: {}", user.getEmail());
        return userConverter.toCreateUserResponse(user);
    }

    public PageResponse<UserResponse> findByEmail(int page, int size, String email) {
        try (Page<User> p = PageHelper.startPage(page, size)) {
            List<UserResponse> users = userMapper.searchByEmail(email);
            PageInfo<User> pageInfo = p.toPageInfo();

            return PageResponse.<UserResponse>builder()
                    .currentPage(pageInfo.getPageNum())
                    .pageSize(pageInfo.getPageSize())
                    .totalPages(pageInfo.getPages())
                    .totalElements(pageInfo.getTotal())
                    .content(users) 
                    .build();
        }
    }

    public UserResponse getUserById(Long id) {
        return userMapper.findById(id);
    }

    public void update(Long id, UpdateUserRequest request) {
        userMapper.update(id, request);
    }

    public UserResponse myInfo(Long userId) {
        return userMapper.findById(userId);
    }

}
