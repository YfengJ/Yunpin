package com.example.employment.mapper;

import com.example.employment.entity.Company;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CompanyMapper {

    List<Company> findAll();

    Company findById(Long id);

    // 🔥 核心修改：统一命名为 selectByUserId，解决 Controller 报错
    Company selectByUserId(Long userId);

    int insert(Company company);

    int update(Company company);

    int deleteById(Long id);
}