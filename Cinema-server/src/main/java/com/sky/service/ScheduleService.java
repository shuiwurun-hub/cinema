package com.sky.service;

import com.sky.dto.ScheduleDTO;
import com.sky.entity.Schedule;
import com.sky.vo.ScheduleVO;

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

    List<ScheduleVO> list();

}