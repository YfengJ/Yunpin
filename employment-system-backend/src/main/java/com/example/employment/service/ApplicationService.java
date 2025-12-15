package com.example.employment.service;

import com.example.employment.entity.Application;
import java.util.List;

public interface ApplicationService {
    // 对应 Controller: getById(id)
    Application getById(Long id);

    // 对应 Controller: getByStudentId(studentId)
    List<Application> getByStudentId(Long studentId);

    // 对应 Controller: getByJobId(jobId)
    List<Application> getByJobId(Long jobId);

    // 对应 Controller: getByStatus(status)
    List<Application> getByStatus(String status);

    // 对应 Controller: getAll()
    List<Application> getAll();

    // 对应 Controller: create(application)
    int save(Application application);

    // 对应 Controller: update(id, application)
    int update(Application application);

    // 对应 Controller: delete(id)
    int deleteById(Long id);
}