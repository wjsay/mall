package com.wjsay.mall.controller;

import com.wjsay.mall.domain.User;
import com.wjsay.mall.result.Result;
import com.wjsay.mall.service.RedisService;
import com.wjsay.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class SampleController {
    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model) {
        model.addAttribute("name", "wjsay");
        return "hello";
    }

    @RequestMapping("/db/get")
    @ResponseBody
    public Result<User>dbGet() {
        User user = userService.getUserById(1);
        return Result.success(user);
    }

    @RequestMapping("/db/tx")
    @ResponseBody
    public Result<Boolean> dbTx() {
        return Result.success(userService.transaction());
    }

    @RequestMapping("redis/get")
    @ResponseBody
    public Result<User> redisGet() {
        redisService.get(String, Class<T> clazz);
    }

}
