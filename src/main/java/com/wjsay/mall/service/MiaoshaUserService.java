package com.wjsay.mall.service;

import com.wjsay.mall.dao.MiaoshaUserDao;
import com.wjsay.mall.domain.MiaoshaUser;
import com.wjsay.mall.execption.GlobalExecption;
import com.wjsay.mall.redis.MiaoshaUserKey;
import com.wjsay.mall.redis.RedisService;
import com.wjsay.mall.result.CodeMsg;
import com.wjsay.mall.result.Result;
import com.wjsay.mall.util.MD5Util;
import com.wjsay.mall.util.UUIDUtil;
import com.wjsay.mall.validator.LoginVo;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Random;

@Service
public class MiaoshaUserService {
    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    MiaoshaUserDao miaoshaUserDao;

    @Autowired
    RedisService redisService;

    public MiaoshaUser getById(long id) {
        return miaoshaUserDao.getById(id);
    }

    public MiaoshaUser getByPhoneNo(String no) {
        return miaoshaUserDao.getByPhoneNo(no);
    }

    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        if (user != null) {
            addCookie(response, token, user);
        }
        return user;
    }

    private void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {
        redisService.set(MiaoshaUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKey.TOKEN_EXPIRE);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public String login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw  new GlobalExecption(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPassword = loginVo.getPassword();
        MiaoshaUser user = getByPhoneNo(mobile);
        if (user == null) {
            throw new GlobalExecption(CodeMsg.MOBILE_NOT_EXIST);
        }
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPassword, saltDB);
        if (!calcPass.equals(dbPass)) {
            throw new GlobalExecption(CodeMsg.PASSWORD_ERROR);
        }
        String token = UUIDUtil.uuid();
        addCookie(response, token, user);
        return token;
    }

    public int addUser(LoginVo loginVo) {
        try {
            MiaoshaUser user = miaoshaUserDao.getByPhoneNo(loginVo.getMobile());
            if (user != null) {
                return -1; // 用户已存在
            }
            user = new MiaoshaUser();
            user.setPhoneno(Long.parseLong(loginVo.getMobile()));
            final String dict = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            StringBuilder buff = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < 6; ++i) {
                buff.append(dict.charAt(random.nextInt(dict.length())));
            }
            user.setSalt(buff.toString());
            user.setPassword(MD5Util.formPassToDBPass(loginVo.getPassword(), user.getSalt()));
            user.setRegisterDate(new Date());
            user.setNickname("mall" + user.getPhoneno());
            miaoshaUserDao.addUser(user);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -2; // 数据库操作异常
        }
    }
}
