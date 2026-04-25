package com.cinema.service;

import com.cinema.dto.HallDTO;
import com.cinema.entity.Hall;
import com.cinema.vo.HallVO;

import java.util.List;

public interface HallService {

    void save(HallDTO hallDTO);

    void update(HallDTO hallDTO);

    void deleteById(Long id);

    void startOrStop(Integer status, Long id);

    HallVO getById(Long id);

    List<Hall> listByCinemaId(Long cinemaId);

    List<Hall> listAll();

}
