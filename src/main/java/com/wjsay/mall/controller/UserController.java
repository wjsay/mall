package com.wjsay.mall.controller;

import com.wjsay.mall.domain.MiaoshaUser;
import com.wjsay.mall.redis.RedisService;
import com.wjsay.mall.result.Result;
import com.wjsay.mall.service.MiaoshaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    MiaoshaUserService  userService;
    @Autowired
    RedisService redisService;

    // 589 tps
    @RequestMapping("/info")
    @ResponseBody
    public Result<MiaoshaUser> info(Model model, MiaoshaUser user) {
        return Result.success(user);
    }
}
