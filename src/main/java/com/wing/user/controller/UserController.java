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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.wing.user.enums.UserTypeEnum.BUYER;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("/login/{openid}")
    public ResultVO login(@PathVariable("openid") String openid,
                          HttpServletRequest request,
                          HttpServletResponse response) {
        Cookie cookie = CookieUtil.get(request, "token");
        if (Objects.nonNull(cookie) &&
                Objects.nonNull(redisTemplate.opsForValue().get(String.format("token_%s", cookie.getValue())))) {
            return ResultVOUtil.success();
        }
        User user = userService.findUserByOpenid(openid);
        if (user == null) {
            throw new NotFoundUserException("Not Found User");
        }
        String token = UUID.randomUUID().toString().replace("-", "");
        String storeContent = openid + "_";
        switch (user.getUserType()) {
            case BUYER:
                storeContent += BUYER;
                break;
            case SELLER:
//              storeContent += SELLER;
                break;
            default:
                new NotFoundUserException("Not Found User by Type");
        }
        redisTemplate.opsForValue().set(String.format("token_%s", token), storeContent, 7200, TimeUnit.SECONDS);
        CookieUtil.set(response, "token", token, 7200);
        return ResultVOUtil.success();
    }
}