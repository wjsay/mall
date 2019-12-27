package com.wjsay.mall.service;

import com.wjsay.mall.dao.UserDao;
import com.wjsay.mall.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    UserDao userDao;

    public User getUserById(int id) {
        return userDao.getUserById(id);
    }
    @Transactional
    public boolean transaction() {
        User user1 = new User();
        user1.setName("Cat");
        User user2 = new User();
        user2.setId(3);
        user2.setName("Dog");
        try {
            userDao.insert(user1);
            userDao.insert(user2);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
