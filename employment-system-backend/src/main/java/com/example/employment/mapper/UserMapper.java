package com.example.employment.mapper;

import com.example.employment.entity.User;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface UserMapper {
    // 这里的 SQL 全部交给 XML 去写，这里只留名字

    User findById(Long id);

    User findByUsername(String username);

    List<User> findAll();

    int insert(User user);

    int update(User user);

    int deleteById(Long id);
}