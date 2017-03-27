package com.rjs.dubbo.test.user;

import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.dubbo.demo.user.User;
import com.alibaba.dubbo.demo.user.UserService;

public class UserServiceImpl implements UserService {

    private final AtomicLong idGen = new AtomicLong();

    public User getUser(Long id) {
        return new User(id, "username" + id);
    }


    public Long registerUser(User user) {
//        System.out.println("Username is " + user.getName());
        return idGen.incrementAndGet();
    }
}
