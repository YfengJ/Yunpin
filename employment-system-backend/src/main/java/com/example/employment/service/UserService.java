package com.example.employment.service;

import com.example.employment.entity.User;

import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 根据ID查询用户
     */
    User getById(Long id);
    
    /**
     * 根据用户名查询用户
     */
    User getByUsername(String username);
    
    /**
     * 查询所有用户
     */
    List<User> getAll();
    
    /**
     * 保存用户
     */
    int save(User user);
    
    /**
     * 更新用户
     */
    int update(User user);
    
    /**
     * 根据ID删除用户
     */
    int deleteById(Long id);
}

