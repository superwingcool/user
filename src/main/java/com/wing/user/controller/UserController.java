package com.wing.user.controller;

import com.wing.user.entity.User;
import com.wing.user.exception.NotFoundUserException;
import com.wing.user.service.UserService;
import com.wing.user.util.CookieUtil;
import com.wing.user.util.ResultVOUtil;
import com.wing.user.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("/login/{openid}")
    public ResultVO login(@PathVariable("openid") String openid, HttpServletResponse response) {
        User user = userService.findUserByOpenid(openid);
        if (user == null) {
            throw new NotFoundUserException("Not Found User");
        }
        String token = UUID.randomUUID().toString().replace("-", "");
        switch (user.getUserType()) {
            case BUYER:
            case SELLER:
                redisTemplate.opsForValue().set(String.format("token_%s", token), openid, 7200, TimeUnit.SECONDS);
                CookieUtil.set(response, "token", token, 7200);
                break;
            default:
                new NotFoundUserException("Not Found User by Type");
        }
        return ResultVOUtil.success();
    }
}