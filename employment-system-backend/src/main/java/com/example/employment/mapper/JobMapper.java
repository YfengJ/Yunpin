package com.example.employment.mapper;

import com.example.employment.entity.Job;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface JobMapper {
    // 🔥 修改：增加 name 参数
    List<Job> findAll(String name);

    Job findById(Long id);
    int insert(Job job);
    int update(Job job);
    int deleteById(Long id);
    int count();
}