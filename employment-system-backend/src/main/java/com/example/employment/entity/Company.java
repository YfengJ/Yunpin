package com.example.employment.entity;

import java.util.Date;

public class Company {
    private Long id;
    private Long userId;
    private String companyName;
    private String logo;
    private String industry;
    private String description;
    private String contactPhone;
    private String address;

    // 🔥 新增：对应数据库的 location 字段
    private String location;

    // 🔥 新增：对应数据库的 status 字段 (active/disabled)
    private String status;

    // 🔥🔥 核心新增：对应数据库的 audit_status 字段 (pending/approved/rejected)
    private String auditStatus;

    private Date createTime;
    private Date updateTime;

    // --- Getter & Setter ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }

    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    // 🔥 新增 location 的 Getter/Setter
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    // 🔥 新增 status 的 Getter/Setter
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // 🔥🔥 新增 auditStatus 的 Getter/Setter (解决报错的关键)
    public String getAuditStatus() { return auditStatus; }
    public void setAuditStatus(String auditStatus) { this.auditStatus = auditStatus; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}