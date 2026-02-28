package com.example.employment.mapper;

import com.example.employment.entity.Application;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param; // 🔥 记得引入 Param
import org.apache.ibatis.annotations.Select; // 🔥 记得引入 Select

import java.util.List;

@Mapper
public interface ApplicationMapper {
    // 1. 根据ID查询
    Application findById(Long id);

    // 2. 查询所有
    List<Application> findAll();

    // 3. 根据学生ID查询
    List<Application> findByStudentId(Long studentId);

    // 4. 根据职位ID查询
    List<Application> findByJobId(Long jobId);

    // 5. 根据状态查询
    List<Application> findByStatus(String status);

    // 6. 根据公司负责人的 UserID 查询
    List<Application> findByCompanyUserId(Long userId);

    // 7. 插入
    int insert(Application application);

    // 8. 更新
    int update(Application application);

    // 9. 删除
    int deleteById(Long id);

    // 🔥🔥 新增：检查是否重复投递 (返回数量，>0 表示已投递)
    @Select("SELECT COUNT(*) FROM application WHERE student_id = #{studentId} AND job_id = #{jobId}")
    int countByStudentAndJob(@Param("studentId") Long studentId, @Param("jobId") Long jobId);
}