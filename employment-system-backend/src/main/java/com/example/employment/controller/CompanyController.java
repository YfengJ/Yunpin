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

    // 录入公司信息
    @PostMapping
    public Result<String> create(@RequestBody Company company) {
        // 新录入的公司默认待审核
        company.setAuditStatus("pending");
        return companyService.save(company) > 0 ? Result.success("创建成功，请等待管理员审核") : Result.error("失败");
    }

    @PutMapping
    public Result<String> update(@RequestBody Company company) {
        return companyService.update(company) > 0 ? Result.success("更新成功") : Result.error("失败");
    }

    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        return companyService.deleteById(id) > 0 ? Result.success("删除成功") : Result.error("失败");
    }

    // 🔥🔥🔥 新增：管理员审核接口 🔥🔥🔥
    @PostMapping("/audit/{id}")
    public Result<String> audit(@PathVariable Long id, @RequestParam String status) {
        // 1. 先查出来
        Company company = companyService.getById(id);
        if (company == null) {
            return Result.error("公司不存在");
        }
        // 2. 修改审核状态 (approved / rejected)
        company.setAuditStatus(status);

        // 3. 更新回数据库
        return companyService.update(company) > 0 ? Result.success("审核操作成功") : Result.error("操作失败");
    }
}