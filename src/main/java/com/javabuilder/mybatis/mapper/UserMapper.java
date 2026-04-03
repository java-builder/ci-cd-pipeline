package com.javabuilder.mybatis.mapper;

import com.javabuilder.mybatis.dto.request.UpdateUserRequest;
import com.javabuilder.mybatis.dto.response.UserResponse;
import com.javabuilder.mybatis.model.User;
import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {

    @Select("SELECT COUNT(*) > 0 FROM users WHERE email = #{email}")
    boolean existsByEmail(String email);

    @Insert("INSERT INTO users(name, email, password) VALUES (#{name}, #{email}, #{password})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(User user);

    @Select("SELECT id, name, email, status FROM users u WHERE #{email} IS NULL OR email LIKE '%' ${email} '%'")
    List<UserResponse> searchByEmail(@Param("email") String email);

    @Select("SELECT id, name, email, status FROM users WHERE id = #{id}")
    UserResponse findById(Long id);

    void update(@Param("id") Long id, @Param("request") UpdateUserRequest request);

    Optional<User> findByEmail(@Param("email") String email);
}
