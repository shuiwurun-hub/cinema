package com.cinema.service.impl;

import com.cinema.entity.Seat;
import com.cinema.mapper.SeatMapper;
import com.cinema.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatServiceImpl implements SeatService {

    @Autowired
    private SeatMapper seatMapper;

    @Override
    public Seat getById(Long id) {
        return seatMapper.getById(id);
    }

    @Override
    public List<Seat> listByHallId(Long hallId) {
        return seatMapper.listByHallId(hallId);
    }

}
