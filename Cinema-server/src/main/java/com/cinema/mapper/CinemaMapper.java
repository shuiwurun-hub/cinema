package com.cinema.mapper;

import com.cinema.entity.Cinema;
import com.cinema.vo.CinemaVO;
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

    @Select("<script>select * from cinema <where> <if test='status != null'> status = #{status} </if> </where> </script>")
    List<Cinema> listByStatus(Integer status);

    @Select("SELECT * FROM cinema WHERE status = 1 ORDER BY RAND() LIMIT 5")
    List<Cinema> getNearby();

}
