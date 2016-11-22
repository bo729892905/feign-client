package com.test.service;

import com.test.feign.UserFeignClient;
import com.test.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ren.xiaobo on 2016/8/29.
 */
@Service
public class UserService {
    @Autowired
    private UserFeignClient userFeignClient;

    public User getUser(long id) {
        return userFeignClient.getUser(id);
    }

    public List<User> getUsers() {
        return userFeignClient.getUsers();
    }
}
