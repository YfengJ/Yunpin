package com.example.employment.service.impl;

import com.example.employment.entity.Job;
import com.example.employment.mapper.JobMapper;
import com.example.employment.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobMapper jobMapper;

    // 🔥 修改：增加 name 参数，传给 Mapper
    @Override
    public List<Job> getAll(String name) {
        return jobMapper.findAll(name);
    }

    // 2. 查询详情
    @Override
    public Job getById(Long id) {
        return jobMapper.findById(id);
    }

    // 3. 新增实现
    @Override
    public int save(Job job) {
        return jobMapper.insert(job);
    }

    // 4. 更新实现
    @Override
    public int update(Job job) {
        return jobMapper.update(job);
    }

    // 5. 删除实现
    @Override
    public int deleteById(Long id) {
        return jobMapper.deleteById(id);
    }

    @Override
    public int count() {
        return jobMapper.count();
    }
}