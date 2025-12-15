package com.example.employment.service.impl;

import com.example.employment.entity.Application;
import com.example.employment.mapper.ApplicationMapper;
import com.example.employment.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private ApplicationMapper applicationMapper;

    @Override
    public Application getById(Long id) {
        // 调用我们刚刚在 Mapper 里补全的 findById
        return applicationMapper.findById(id);
    }

    @Override
    public List<Application> getByStudentId(Long studentId) {
        return applicationMapper.findByStudentId(studentId);
    }

    @Override
    public List<Application> getByJobId(Long jobId) {
        return applicationMapper.findByJobId(jobId);
    }

    @Override
    public List<Application> getByStatus(String status) {
        return applicationMapper.findByStatus(status);
    }

    @Override
    public List<Application> getAll() {
        return applicationMapper.findAll();
    }

    @Override
    public int save(Application application) {
        return applicationMapper.insert(application);
    }

    @Override
    public int update(Application application) {
        return applicationMapper.update(application);
    }

    @Override
    public int deleteById(Long id) {
        return applicationMapper.deleteById(id);
    }
}