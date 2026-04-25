package com.cinema.service;

import com.cinema.dto.UserRegisterDTO;
import com.cinema.entity.User;

public interface UserService {
    
    User register(UserRegisterDTO userRegisterDTO);
    
    User login(String username, String password);
}
