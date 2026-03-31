package com.sky.mapper;

import com.sky.entity.Hall;
import com.sky.vo.HallVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HallMapper {

    void insert(Hall hall);

    void update(Hall hall);

    void deleteById(Long id);

    Hall getById(Long id);

    HallVO getVOById(Long id);

    @Select("select * from hall where cinema_id = #{cinemaId}")
    List<Hall> listByCinemaId(Long cinemaId);

    @Select("select * from hall")
    List<Hall> listAll();

}