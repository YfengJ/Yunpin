package com.example.employment.controller;

import com.example.employment.common.Result;
import com.example.employment.entity.Application;
import com.example.employment.mapper.ApplicationMapper;
import com.example.employment.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ApplicationMapper applicationMapper;

    @GetMapping
    public Result<List<Application>> getList(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String role
    ) {
        List<Application> list;
        if ("STUDENT".equalsIgnoreCase(role)) {
            list = applicationMapper.findByStudentId(userId);
        } else if ("COMPANY".equalsIgnoreCase(role)) {
            list = applicationMapper.findByCompanyUserId(userId);
        } else {
            list = applicationMapper.findAll();
        }
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<Application> getById(@PathVariable Long id) {
        return Result.success(applicationService.getById(id));
    }

    @PostMapping
    public Result<String> create(@RequestBody Application application) {
        // 🔥🔥 新增：重复投递校验
        int count = applicationMapper.countByStudentAndJob(application.getStudentId(), application.getJobId());
        if (count > 0) {
            return Result.error("您已投递过该职位，请耐心等待结果，不要重复投递哦！");
        }

        if (application.getStatus() == null) application.setStatus("PENDING");
        int res = applicationService.save(application);
        return res > 0 ? Result.success("投递成功") : Result.error("投递失败");
    }

    @PutMapping("/{id}")
    public Result<String> update(@PathVariable Long id, @RequestBody Application application) {
        application.setId(id);
        int res = applicationService.update(application);
        return res > 0 ? Result.success("更新成功") : Result.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        int res = applicationService.deleteById(id);
        return res > 0 ? Result.success("删除成功") : Result.error("删除失败");
    }
}