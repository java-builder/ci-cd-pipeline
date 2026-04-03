package com.javabuilder.mybatis.mapper;

import com.javabuilder.mybatis.model.Role;
import org.apache.ibatis.annotations.*;
import java.util.Optional;

@Mapper
public interface RoleMapper {

    @Select("SELECT * FROM roles WHERE name = #{name}")
    Optional<Role> findByName(@Param("name") String name);

    @Insert("INSERT INTO roles(name, description) VALUES (#{name}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Role role);
}
