package com.example.employment.service;

import com.example.employment.entity.Job;
import java.util.List;

public interface JobService {
    // 🔥 修改：增加 name 参数
    List<Job> getAll(String name);

    Job getById(Long id);

    int save(Job job);

    int update(Job job);

    int deleteById(Long id);

    int count();
}