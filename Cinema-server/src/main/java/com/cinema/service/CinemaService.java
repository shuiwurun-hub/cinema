package com.cinema.service;

import com.cinema.dto.CinemaDTO;
import com.cinema.entity.Cinema;
import com.cinema.vo.CinemaVO;

import java.util.List;

public interface CinemaService {

    void save(CinemaDTO cinemaDTO);

    void update(CinemaDTO cinemaDTO);

    void deleteById(Long id);

    void startOrStop(Integer status, Long id);

    CinemaVO getById(Long id);

    List<CinemaVO> list();

    List<Cinema> getNearby();

    List<Cinema> listByStatus(Integer status);

}
