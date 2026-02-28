package com.example.employment.mapper;

import com.example.employment.entity.Favorite;
import com.example.employment.entity.Job;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FavoriteMapper {

    @Insert("INSERT INTO favorite (user_id, job_id, create_time) VALUES (#{userId}, #{jobId}, NOW())")
    void insert(Favorite favorite);

    @Delete("DELETE FROM favorite WHERE user_id = #{userId} AND job_id = #{jobId}")
    void deleteByUserAndJob(@Param("userId") Long userId, @Param("jobId") Long jobId);

    @Select("SELECT COUNT(*) FROM favorite WHERE user_id = #{userId} AND job_id = #{jobId}")
    int countByUserAndJob(@Param("userId") Long userId, @Param("jobId") Long jobId);

    @Select("SELECT job_id FROM favorite WHERE user_id = #{userId}")
    List<Long> findJobIdsByUserId(Long userId);

    // 🔥 新增：直接查出该用户收藏的所有职位详情 (带公司Logo)
    @Select("SELECT j.*, c.company_name, c.logo AS company_logo " +
            "FROM favorite f " +
            "JOIN job j ON f.job_id = j.id " +
            "LEFT JOIN company c ON j.company_id = c.id " +
            "WHERE f.user_id = #{userId} " +
            "ORDER BY f.create_time DESC")
    List<Job> findFavoriteJobsByUserId(Long userId);
}