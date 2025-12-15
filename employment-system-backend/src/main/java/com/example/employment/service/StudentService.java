package com.example.employment.service;

import com.example.employment.entity.Student;

import java.util.List;

/**
 * 学生服务接口
 */
public interface StudentService {
    
    /**
     * 根据ID查询学生
     */
    Student getById(Long id);
    
    /**
     * 根据用户ID查询学生
     */
    Student getByUserId(Long userId);
    
    /**
     * 查询所有学生
     */
    List<Student> getAll();
    
    /**
     * 保存学生
     */
    int save(Student student);
    
    /**
     * 更新学生
     */
    int update(Student student);
    
    /**
     * 根据ID删除学生
     */
    int deleteById(Long id);
}

