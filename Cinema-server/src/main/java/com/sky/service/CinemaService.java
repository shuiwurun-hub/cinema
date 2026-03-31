package com.sky.service;

import com.sky.dto.CinemaDTO;
import com.sky.entity.Cinema;
import com.sky.vo.CinemaVO;

import java.util.List;

public interface CinemaService {

    void save(CinemaDTO cinemaDTO);

    void update(CinemaDTO cinemaDTO);

    void deleteById(Long id);

    void startOrStop(Integer status, Long id);

    CinemaVO getById(Long id);

    List<CinemaVO> list();

    List<Cinema> listByStatus(Integer status);

}