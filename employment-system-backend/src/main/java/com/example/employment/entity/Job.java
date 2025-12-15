package com.example.employment.entity;

import java.util.Date;

/**
 * 职位实体类 (手动 Getter/Setter 版)
 */
public class Job {
    private Long id;
    private Long companyId;
    private String jobName;     // 对应数据库的 job_name
    private String salary;
    private String description;
    private String status;
    private Date createTime;
    private Date updateTime;

    // 额外的字段（如果 XML 里关联查询了公司名）
    private String companyName;

    // --- 手动 Getter / Setter ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }

    public String getJobName() { return jobName; }
    public void setJobName(String jobName) { this.jobName = jobName; }

    public String getSalary() { return salary; }
    public void setSalary(String salary) { this.salary = salary; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
}