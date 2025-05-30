package com.example.springai01.mapper;

import com.example.springai01.entity.po.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 学科表 Mapper 接口
 * </p>
 *
 * @author guang
 * @since 2025-05-29
 */
@Mapper
public interface CourseMapper extends BaseMapper<Course> {

}
