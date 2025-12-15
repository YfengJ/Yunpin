package com.example.employment.controller;

import com.example.employment.common.Result;
import com.example.employment.entity.Job;
import com.example.employment.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin
public class JobController {

    @Autowired
    private JobService jobService;

    // 🔥 修改：增加 name 参数，如果没有传就是一个空字符串
    @GetMapping
    public Result<List<Job>> getAll(@RequestParam(defaultValue = "") String name) {
        return Result.success(jobService.getAll(name));
    }

    @GetMapping("/{id}")
    public Result<Job> getById(@PathVariable Long id) { return Result.success(jobService.getById(id)); }

    @PostMapping
    public Result<String> create(@RequestBody Job job) {
        if (job.getCompanyId() == null) job.setCompanyId(1L);
        return jobService.save(job) > 0 ? Result.success("创建成功") : Result.error("失败");
    }

    @PutMapping
    public Result<String> update(@RequestBody Job job) {
        return jobService.update(job) > 0 ? Result.success("更新成功") : Result.error("失败");
    }

    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        return jobService.deleteById(id) > 0 ? Result.success("删除成功") : Result.error("失败");
    }

    @GetMapping("/count")
    public Result<Integer> getCount() {
        return Result.success(jobService.count());
    }
}