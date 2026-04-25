package com.cinema.mapper;

import com.cinema.entity.Hall;
import com.cinema.vo.HallVO;
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
