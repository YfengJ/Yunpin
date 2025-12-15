package com.example.employment.controller;

import com.example.employment.common.Result;
import com.example.employment.entity.Student;
import com.example.employment.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@CrossOrigin
public class StudentController {

    @Autowired
    private StudentMapper studentMapper;

    // 获取我的简历信息
    @GetMapping("/info")
    public Result<Student> getInfo(@RequestParam Long userId) {
        Student student = studentMapper.selectByUserId(userId);
        if (student == null) {
            return Result.success(new Student());
        }
        return Result.success(student);
    }

    // 🔥 修复后的保存逻辑：防重复
    @PutMapping("/info")
    public Result<String> updateInfo(@RequestBody Student student) {
        // 1. 先根据 userId 去数据库查，看是否已存在档案
        Student exist = studentMapper.selectByUserId(student.getUserId());

        if (exist != null) {
            // 2. 如果存在，强制把 ID 设为数据库里的 ID，然后执行更新
            student.setId(exist.getId());
            studentMapper.update(student);
        } else {
            // 3. 如果真不存在，才执行插入
            studentMapper.insert(student);
        }

        return Result.success("简历保存成功");
    }
}