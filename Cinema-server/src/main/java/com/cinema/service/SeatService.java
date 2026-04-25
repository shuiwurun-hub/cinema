package com.cinema.service;

import com.cinema.entity.Seat;

import java.util.List;

public interface    SeatService {

    Seat getById(Long id);

    List<Seat> listByHallId(Long hallId);

}
