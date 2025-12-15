package com.example.employment.entity;

import java.time.LocalDateTime;

public class Application {

    private Long id;
    private Long studentId;
    private Long jobId;
    private String status;      // PENDING, ACCEPTED, REJECTED
    private LocalDateTime createTime;

    private String reason;            // 拒绝理由
    private String interviewTime;     // 面试时间
    private String interviewLocation; // 面试地点
    private String interviewContact;  // 🔥 新增：面试联系方式

    // 关联对象
    private Student student;
    private Job job;

    // --- Getter & Setter ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getInterviewTime() { return interviewTime; }
    public void setInterviewTime(String interviewTime) { this.interviewTime = interviewTime; }
    public String getInterviewLocation() { return interviewLocation; }
    public void setInterviewLocation(String interviewLocation) { this.interviewLocation = interviewLocation; }

    // 🔥 新增 Getter/Setter
    public String getInterviewContact() { return interviewContact; }
    public void setInterviewContact(String interviewContact) { this.interviewContact = interviewContact; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public Job getJob() { return job; }
    public void setJob(Job job) { this.job = job; }
}