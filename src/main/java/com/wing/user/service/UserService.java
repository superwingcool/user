package com.wing.user.service;

import com.wing.user.entity.User;
import com.wing.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findUserByOpenid(String openid) {
        return userRepository.findByOpenid(openid);
    }
}
