package com.example.employment.controller;

import com.example.employment.common.Result;
import com.example.employment.mapper.DashboardDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardDataMapper dashboardMapper;

    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats() {
        Map<String, Object> map = new HashMap<>();

        // 1. 核心卡片数据 (真实)
        map.put("studentCount", dashboardMapper.countStudents());
        map.put("companyCount", dashboardMapper.countCompanies());
        map.put("jobCount", dashboardMapper.countJobs());
        map.put("applicationCount", dashboardMapper.countApplications());

        // 2. 饼图数据 (真实)
        List<Map<String, Object>> industryStats = dashboardMapper.countJobsByIndustry();
        map.put("industryPie", industryStats);

        // 3. 折线图数据 (真实计算)
        map.put("trendLine", buildTrendData());

        return Result.success(map);
    }

    /**
     * 构建最近 7 天的完整趋势数据（自动补 0）
     */
    private Map<String, Object> buildTrendData() {
        // 1. 获取数据库统计结果
        List<Map<String, Object>> appTrend = dashboardMapper.getApplicationTrend();
        List<Map<String, Object>> jobTrend = dashboardMapper.getJobTrend();

        // 2. 准备日期列表（最近 7 天）
        List<String> dates = new ArrayList<>();
        List<Integer> appCounts = new ArrayList<>();
        List<Integer> jobCounts = new ArrayList<>();

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 循环生成过去 7 天的日期 (从6天前到今天)
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            String dateStr = date.format(formatter);
            dates.add(date.format(DateTimeFormatter.ofPattern("MM-dd"))); // 前端显示格式：12-16

            // 查找该日期是否有数据，没有则填 0
            appCounts.add(findCountByDate(appTrend, dateStr));
            jobCounts.add(findCountByDate(jobTrend, dateStr));
        }

        return Map.of(
                "dates", dates,
                "values", appCounts, // 蓝线：投递量
                "views", jobCounts   // 绿线：新增职位量
        );
    }

    // 辅助方法：从数据库结果列表中查找对应日期的数量
    private int findCountByDate(List<Map<String, Object>> list, String targetDate) {
        for (Map<String, Object> map : list) {
            String dbDate = (String) map.get("date"); // 数据库查出来是 yyyy-MM-dd
            if (targetDate.equals(dbDate)) {
                return ((Number) map.get("count")).intValue();
            }
        }
        return 0;
    }
}