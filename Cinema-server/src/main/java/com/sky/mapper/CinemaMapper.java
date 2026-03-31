package com.sky.mapper;

import com.sky.entity.Cinema;
import com.sky.vo.CinemaVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CinemaMapper {

    void insert(Cinema cinema);

    void update(Cinema cinema);

    void deleteById(Long id);

    Cinema getById(Long id);

    List<CinemaVO> list();

    @Select("select * from cinema where status = #{status}")
    List<Cinema> listByStatus(Integer status);

}