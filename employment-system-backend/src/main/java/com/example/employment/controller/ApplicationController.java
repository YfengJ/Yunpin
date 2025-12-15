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

    // 🔥 额外注入 Mapper，专门处理复杂的列表查询，不破坏原有的 Service 结构
    @Autowired
    private ApplicationMapper applicationMapper;

    /**
     * 🔥 智能列表查询接口
     * 前端传 userId 和 role 过来，后端自动决定给看什么数据
     */
    @GetMapping
    public Result<List<Application>> getList(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String role
    ) {
        List<Application> list;

        if ("STUDENT".equalsIgnoreCase(role)) {
            // 学生：只看自己投的
            list = applicationMapper.findByStudentId(userId);
        } else if ("COMPANY".equalsIgnoreCase(role)) {
            // 企业：只看投给自己公司的
            list = applicationMapper.findByCompanyUserId(userId);
        } else {
            // 管理员：看所有 (默认逻辑)
            list = applicationMapper.findAll();
        }

        return Result.success(list);
    }

    // --- 其他接口保持不变 ---

    @GetMapping("/{id}")
    public Result<Application> getById(@PathVariable Long id) {
        return Result.success(applicationService.getById(id));
    }

    @PostMapping
    public Result<String> create(@RequestBody Application application) {
        if (application.getStatus() == null) application.setStatus("PENDING");
        int res = applicationService.save(application);
        return res > 0 ? Result.success("创建成功") : Result.error("创建失败");
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