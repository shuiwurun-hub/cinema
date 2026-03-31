package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.HallDTO;
import com.sky.entity.Hall;
import com.sky.entity.Seat;
import com.sky.mapper.HallMapper;
import com.sky.mapper.SeatMapper;
import com.sky.service.HallService;
import com.sky.vo.HallVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HallServiceImpl implements HallService {

    @Autowired
    private HallMapper hallMapper;

    @Autowired
    private SeatMapper seatMapper;

    @Override
    @Transactional
    public void save(HallDTO hallDTO) {
        Hall hall = new Hall();
        BeanUtils.copyProperties(hallDTO, hall);

        hall.setStatus(1);
        hall.setTotalSeats(hallDTO.getRowCount() * hallDTO.getColCount());

        hall.setCreateTime(LocalDateTime.now());
        hall.setUpdateTime(LocalDateTime.now());
        hall.setCreateUser(BaseContext.getCurrentId());
        hall.setUpdateUser(BaseContext.getCurrentId());

        hallMapper.insert(hall);

        generateSeats(hall.getId(), hallDTO.getRowCount(), hallDTO.getColCount());
    }

    private void generateSeats(Long hallId, Integer rowCount, Integer colCount) {
        for (int row = 1; row <= rowCount; row++) {
            for (int col = 1; col <= colCount; col++) {
                String seatType = "普通";
                if (row <= 2) {
                    seatType = "VIP";
                }

                Seat seat = Seat.builder()
                        .hallId(hallId)
                        .rowNum(row)
                        .colNum(col)
                        .seatType(seatType)
                        .status(1)
                        .build();
                seatMapper.insert(seat);
            }
        }
    }

    @Override
    public void update(HallDTO hallDTO) {
        Hall hall = new Hall();
        BeanUtils.copyProperties(hallDTO, hall);

        hall.setUpdateTime(LocalDateTime.now());
        hall.setUpdateUser(BaseContext.getCurrentId());

        hallMapper.update(hall);
    }

    @Override
    public void deleteById(Long id) {
        hallMapper.deleteById(id);
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        Hall hall = Hall.builder()
                .id(id)
                .status(status)
                .updateTime(LocalDateTime.now())
                .updateUser(BaseContext.getCurrentId())
                .build();
        hallMapper.update(hall);
    }

    @Override
    public HallVO getById(Long id) {
        return hallMapper.getVOById(id);
    }

    @Override
    public List<Hall> listByCinemaId(Long cinemaId) {
        return hallMapper.listByCinemaId(cinemaId);
    }

    @Override
    public List<Hall> listAll() {
        return hallMapper.listAll();
    }

}