package com.example.employment.controller;

import com.example.employment.common.Result;
import com.example.employment.entity.Company;
import com.example.employment.entity.Student;
import com.example.employment.entity.User;
import com.example.employment.mapper.CompanyMapper;
import com.example.employment.mapper.StudentMapper;
import com.example.employment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private StudentMapper studentMapper;

    // 登录
    @PostMapping("/login")
    public Result<User> login(@RequestBody User user) {
        User dbUser = userService.getByUsername(user.getUsername());
        if (dbUser == null) return Result.error("账号不存在");
        if (!dbUser.getPassword().equals(user.getPassword())) return Result.error("密码错误");
        return Result.success(dbUser);
    }

    // 注册
    @PostMapping("/register")
    public Result<String> register(@RequestBody User user) {
        User exist = userService.getByUsername(user.getUsername());
        if (exist != null) return Result.error("该账号已存在");

        if (user.getRole() == null || user.getRole().isEmpty()) user.setRole("student");

        userService.save(user);

        // 自动创建关联档案
        if ("company".equalsIgnoreCase(user.getRole())) {
            try {
                Company company = new Company();
                company.setCompanyName(user.getUsername() + " (未完善)");
                company.setUserId(user.getId());
                companyMapper.insert(company);
            } catch(Exception e) { e.printStackTrace(); }
        }

        if ("student".equalsIgnoreCase(user.getRole())) {
            try {
                Student student = new Student();
                student.setFullname(user.getUsername());
                student.setUserId(user.getId());
                studentMapper.insert(student);
            } catch(Exception e) { e.printStackTrace(); }
        }

        return Result.success("注册成功");
    }

    // 🔥 1. 获取所有用户列表 (用于管理员管理页面)
    @GetMapping
    public Result<List<User>> getAll() {
        return Result.success(userService.getAll());
    }

    // 🔥 2. 删除用户 (级联删除：同时删除关联的学生或企业信息)
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        // 先查询该用户
        User user = userService.getById(id);
        if (user == null) return Result.error("用户不存在");

        // 如果是学生，先删除学生档案
        if ("STUDENT".equalsIgnoreCase(user.getRole())) {
            Student s = studentMapper.selectByUserId(id);
            if (s != null) {
                studentMapper.deleteById(s.getId());
            }
        }

        // 如果是企业，先删除企业信息
        if ("COMPANY".equalsIgnoreCase(user.getRole())) {
            Company c = companyMapper.selectByUserId(id);
            if (c != null) {
                companyMapper.deleteById(c.getId());
            }
        }

        // 最后删除账号
        return userService.deleteById(id) > 0 ? Result.success("删除成功") : Result.error("失败");
    }

    // 🔥 3. 重置密码 (管理员功能)
    @PutMapping("/{id}/reset-password")
    public Result<String> resetPassword(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) return Result.error("用户不存在");

        user.setPassword("123456"); // 重置为默认密码
        return userService.update(user) > 0 ? Result.success("密码已重置为 123456") : Result.error("重置失败");
    }

    // 获取单个详情
    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }

    // 更新信息
    @PutMapping("/{id}")
    public Result<String> update(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        return userService.update(user) > 0 ? Result.success("更新成功") : Result.error("失败");
    }
}