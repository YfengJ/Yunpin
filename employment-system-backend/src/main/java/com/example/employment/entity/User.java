package com.example.employment.entity;

import java.util.Date;

/**
 * 用户实体类
 * 对应数据库表: user
 */
public class User {
    private Long id;
    private String username;
    private String password;
    private String role; // 对应数据库值: admin, company, student
    private Date createTime;
    private Date updateTime;

    // --- Getter 和 Setter ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}