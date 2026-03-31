package com.sky.service;

import com.sky.dto.HallDTO;
import com.sky.entity.Hall;
import com.sky.vo.HallVO;

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