package com.example.employment.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

@Mapper
public interface DashboardDataMapper {
    // 基础统计
    @Select("SELECT count(*) FROM student")
    int countStudents();

    @Select("SELECT count(*) FROM company")
    int countCompanies();

    @Select("SELECT count(*) FROM job WHERE status = 'active'")
    int countJobs();

    @Select("SELECT count(*) FROM application")
    int countApplications();

    // 饼图：统计各行业的职位数量 (真实数据)
    @Select("SELECT c.industry AS name, COUNT(j.id) AS value " +
            "FROM job j " +
            "LEFT JOIN company c ON j.company_id = c.id " +
            "WHERE c.industry IS NOT NULL AND c.industry != '' " +
            "GROUP BY c.industry " +
            "ORDER BY value DESC LIMIT 6")
    List<Map<String, Object>> countJobsByIndustry();

    // 🔥 新增：统计最近 7 天的每日投递量
    @Select("SELECT DATE_FORMAT(create_time, '%Y-%m-%d') as date, COUNT(*) as count " +
            "FROM application " +
            "WHERE create_time >= DATE_SUB(CURDATE(), INTERVAL 6 DAY) " +
            "GROUP BY date " +
            "ORDER BY date ASC")
    List<Map<String, Object>> getApplicationTrend();

    // 🔥 新增：统计最近 7 天的每日新增职位
    @Select("SELECT DATE_FORMAT(create_time, '%Y-%m-%d') as date, COUNT(*) as count " +
            "FROM job " +
            "WHERE create_time >= DATE_SUB(CURDATE(), INTERVAL 6 DAY) " +
            "GROUP BY date " +
            "ORDER BY date ASC")
    List<Map<String, Object>> getJobTrend();
}