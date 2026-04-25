package com.cinema.mapper;

import com.cinema.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("select * from user where id = #{id}")
    User getById(Long id);
    
    @Select("select * from user where phone = #{phone}")
    User getByPhone(String phone);
    
    @Select("select * from user where username = #{username}")
    User getByUsername(String username);

    void insert(User user);
}
