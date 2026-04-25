package com.cinema.service.impl;

import com.cinema.context.BaseContext;
import com.cinema.dto.ScheduleDTO;
import com.cinema.entity.Schedule;
import com.cinema.mapper.ScheduleMapper;
import com.cinema.service.ScheduleService;
import com.cinema.vo.ScheduleVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Override
    public void save(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO, schedule);

        schedule.setStatus(1);

        schedule.setCreateTime(LocalDateTime.now());
        schedule.setUpdateTime(LocalDateTime.now());
        schedule.setCreateUser(BaseContext.getCurrentId());
        schedule.setUpdateUser(BaseContext.getCurrentId());

        scheduleMapper.insert(schedule);
    }

    @Override
    public void update(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO, schedule);

        schedule.setUpdateTime(LocalDateTime.now());
        schedule.setUpdateUser(BaseContext.getCurrentId());

        scheduleMapper.update(schedule);
    }

    @Override
    public void deleteById(Long id) {
        scheduleMapper.deleteById(id);
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        Schedule schedule = Schedule.builder()
                .id(id)
                .status(status)
                .updateTime(LocalDateTime.now())
                .updateUser(BaseContext.getCurrentId())
                .build();
        scheduleMapper.update(schedule);
    }

    @Override
    public ScheduleVO getById(Long id) {
        return scheduleMapper.getVOById(id);
    }

    @Override
    public List<ScheduleVO> listByMovieId(Long movieId, LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        return scheduleMapper.listByMovieId(movieId, date);
    }

    @Override
    public List<ScheduleVO> listByCinemaId(Long cinemaId, LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        return scheduleMapper.listByCinemaId(cinemaId, date);
    }

    @Override
    public List<ScheduleVO> listByHallId(Long hallId) {
        return scheduleMapper.listByHallId(hallId);
    }

    @Override
    public List<ScheduleVO> list() {
        return scheduleMapper.listAll();
    }

    @Override
    public List<ScheduleVO> listWithFilters(Long movieId, Long cinemaId, LocalDate showDate) {
        return scheduleMapper.listWithFilters(movieId, cinemaId, showDate);
    }

}
