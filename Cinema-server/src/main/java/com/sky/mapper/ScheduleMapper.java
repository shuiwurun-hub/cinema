package com.sky.mapper;

import com.sky.entity.Schedule;
import com.sky.vo.ScheduleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ScheduleMapper {

    void insert(Schedule schedule);

    void update(Schedule schedule);

    void deleteById(Long id);

    Schedule getById(Long id);

    ScheduleVO getVOById(Long id);

    @Select("select * from schedule where movie_id = #{movieId} and show_date >= #{date} and status = 1 order by show_date, show_time")
    List<ScheduleVO> listByMovieId(Long movieId, LocalDate date);

    @Select("select * from schedule where cinema_id = #{cinemaId} and show_date >= #{date} and status = 1 order by show_date, show_time")
    List<ScheduleVO> listByCinemaId(Long cinemaId, LocalDate date);

    @Select("select * from schedule where status = 1 order by show_date, show_time")
    List<ScheduleVO> listAll();

}