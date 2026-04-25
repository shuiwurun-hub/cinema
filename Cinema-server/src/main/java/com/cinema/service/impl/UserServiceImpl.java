package com.cinema.service.impl;

import com.cinema.constant.MessageConstant;
import com.cinema.dto.UserRegisterDTO;
import com.cinema.entity.User;
import com.cinema.exception.LoginFailedException;
import com.cinema.exception.RegisterFailedException;
import com.cinema.mapper.UserMapper;
import com.cinema.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User register(UserRegisterDTO userRegisterDTO) {
        User existUser = userMapper.getByUsername(userRegisterDTO.getUsername());
        if (existUser != null) {
            throw new RegisterFailedException("用户名已存在");
        }
        
        User user = User.builder()
                .username(userRegisterDTO.getUsername())
                .email(userRegisterDTO.getEmail())
                .password(userRegisterDTO.getPassword())
                .createTime(LocalDateTime.now())
                .build();
        
        userMapper.insert(user);
        return user;
    }

    @Override
    public User login(String username, String password) {
        User user = userMapper.getByUsername(username);
        if (user == null) {
            throw new LoginFailedException("用户名或密码错误");
        }
        
        if (!user.getPassword().equals(password)) {
            throw new LoginFailedException("用户名或密码错误");
        }
        
        return user;
    }
}
