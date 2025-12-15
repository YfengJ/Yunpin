package com.example.employment.mapper;

import com.example.employment.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 纯净版 Mapper 接口
 * 没有任何 @Select/@Insert 注解，完全依赖 resources/mapper/StudentMapper.xml
 */
@Mapper
public interface StudentMapper {

    // 对应 XML 中的 id="selectById"
    Student selectById(Long id);

    // 对应 XML 中的 id="selectByUserId"
    Student selectByUserId(Long userId);

    // 对应 XML 中的 id="selectAll"
    List<Student> selectAll();

    // 对应 XML 中的 id="insert"
    int insert(Student student);

    // 对应 XML 中的 id="update"
    int update(Student student);

    // 对应 XML 中的 id="deleteById"
    int deleteById(Long id);
}