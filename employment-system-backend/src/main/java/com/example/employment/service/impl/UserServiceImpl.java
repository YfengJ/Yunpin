package com.example.employment.service.impl;

import com.example.employment.entity.User;
import com.example.employment.mapper.UserMapper;
import com.example.employment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getById(Long id) {
        return userMapper.findById(id);
    }

    @Override
    public User getByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public List<User> getAll() {
        return userMapper.findAll();
    }

    @Override
    public int save(User user) {
        // 如果没有创建时间，给一个当前时间
        if (user.getCreateTime() == null) {
            user.setCreateTime(new Date());
        }
        // 更新时间也设为当前
        user.setUpdateTime(new Date());
        return userMapper.insert(user);
    }

    @Override
    public int update(User user) {
        user.setUpdateTime(new Date());
        return userMapper.update(user);
    }

    @Override
    public int deleteById(Long id) {
        return userMapper.deleteById(id);
    }
}