package com.cinema.mapper;

import com.cinema.entity.Schedule;
import com.cinema.vo.ScheduleVO;
import org.apache.ibatis.annotations.Delete;
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

    List<ScheduleVO> listByMovieId(Long movieId, LocalDate date);

    List<ScheduleVO> listByCinemaId(Long cinemaId, LocalDate date);

    @Select("SELECT s.*, m.name as movieName, c.name as cinemaName, h.name as hallName " +
            "FROM schedule s " +
            "LEFT JOIN movie m ON s.movie_id = m.id " +
            "LEFT JOIN cinema c ON s.cinema_id = c.id " +
            "LEFT JOIN hall h ON s.hall_id = h.id " +
            "WHERE s.hall_id = #{hallId} " +
            "ORDER BY s.show_date DESC, s.show_time ASC")
    List<ScheduleVO> listByHallId(Long hallId);

    List<ScheduleVO> listAll();

    List<ScheduleVO> listWithFilters(Long movieId, Long cinemaId, LocalDate showDate);

    @Delete("DELETE FROM schedule WHERE movie_id = #{movieId}")
    void deleteByMovieId(Long movieId);

}
