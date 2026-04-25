package com.cinema.service;

import com.cinema.dto.ScheduleDTO;
import com.cinema.entity.Schedule;
import com.cinema.vo.ScheduleVO;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {

    void save(ScheduleDTO scheduleDTO);

    void update(ScheduleDTO scheduleDTO);

    void deleteById(Long id);

    void startOrStop(Integer status, Long id);

    ScheduleVO getById(Long id);

    List<ScheduleVO> listByMovieId(Long movieId, LocalDate date);

    List<ScheduleVO> listByCinemaId(Long cinemaId, LocalDate date);

    List<ScheduleVO> listByHallId(Long hallId);

    List<ScheduleVO> list();

    List<ScheduleVO> listWithFilters(Long movieId, Long cinemaId, LocalDate showDate);

}
