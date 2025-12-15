package com.example.employment.service.impl;

import com.example.employment.entity.Student;
import com.example.employment.mapper.StudentMapper;
import com.example.employment.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 学生服务实现类
 */
@Service
public class StudentServiceImpl implements StudentService {
    
    @Autowired
    private StudentMapper studentMapper;
    
    @Override
    public Student getById(Long id) {
        return studentMapper.selectById(id);
    }
    
    @Override
    public Student getByUserId(Long userId) {
        return studentMapper.selectByUserId(userId);
    }
    
    @Override
    public List<Student> getAll() {
        return studentMapper.selectAll();
    }
    
    @Override
    public int save(Student student) {
        if (student.getCreateTime() == null) {
            student.setCreateTime(LocalDateTime.now());
        }
        if (student.getUpdateTime() == null) {
            student.setUpdateTime(LocalDateTime.now());
        }
        return studentMapper.insert(student);
    }
    
    @Override
    public int update(Student student) {
        student.setUpdateTime(LocalDateTime.now());
        return studentMapper.update(student);
    }
    
    @Override
    public int deleteById(Long id) {
        return studentMapper.deleteById(id);
    }
}

