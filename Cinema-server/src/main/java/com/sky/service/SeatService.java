package com.sky.service;

import com.sky.entity.Seat;

import java.util.List;

public interface SeatService {

    Seat getById(Long id);

    List<Seat> listByHallId(Long hallId);

}
