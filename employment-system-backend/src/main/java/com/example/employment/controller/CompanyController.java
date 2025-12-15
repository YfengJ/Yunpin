package com.example.employment.controller;

import com.example.employment.common.Result;
import com.example.employment.entity.Company;
import com.example.employment.mapper.CompanyMapper;
import com.example.employment.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/companies")
@CrossOrigin
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyMapper companyMapper;

    /**
     * 获取公司列表
     */
    @GetMapping
    public Result<List<Company>> getAll(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String role
    ) {
        // 如果是企业用户 (COMPANY)，只查自己关联的那一家公司
        if ("COMPANY".equalsIgnoreCase(role) && userId != null) {

            // 🔥🔥🔥 核心修复：这里改成 selectByUserId 🔥🔥🔥
            Company myCompany = companyMapper.selectByUserId(userId);

            if (myCompany != null) {
                return Result.success(Collections.singletonList(myCompany));
            } else {
                return Result.success(Collections.emptyList());
            }
        }

        // 管理员查所有
        return Result.success(companyMapper.findAll());
    }

    @GetMapping("/{id}")
    public Result<Company> getById(@PathVariable Long id) {
        return Result.success(companyService.getById(id));
    }

    @PostMapping
    public Result<String> create(@RequestBody Company company) {
        return companyService.save(company) > 0 ? Result.success("创建成功") : Result.error("失败");
    }

    @PutMapping
    public Result<String> update(@RequestBody Company company) {
        return companyService.update(company) > 0 ? Result.success("更新成功") : Result.error("失败");
    }

    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        return companyService.deleteById(id) > 0 ? Result.success("删除成功") : Result.error("失败");
    }
}