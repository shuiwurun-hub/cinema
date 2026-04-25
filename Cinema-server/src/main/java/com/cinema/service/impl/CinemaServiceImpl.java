package com.cinema.service.impl;

import com.cinema.context.BaseContext;
import com.cinema.dto.CinemaDTO;
import com.cinema.entity.Cinema;
import com.cinema.mapper.CinemaMapper;
import com.cinema.service.CinemaService;
import com.cinema.vo.CinemaVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CinemaServiceImpl implements CinemaService {

    @Autowired
    private CinemaMapper cinemaMapper;

    @Override
    public void save(CinemaDTO cinemaDTO) {
        Cinema cinema = new Cinema();
        BeanUtils.copyProperties(cinemaDTO, cinema);

        cinema.setStatus(1);

        cinema.setCreateTime(LocalDateTime.now());
        cinema.setUpdateTime(LocalDateTime.now());
        cinema.setCreateUser(BaseContext.getCurrentId());
        cinema.setUpdateUser(BaseContext.getCurrentId());

        cinemaMapper.insert(cinema);
    }

    @Override
    public void update(CinemaDTO cinemaDTO) {
        Cinema cinema = new Cinema();
        BeanUtils.copyProperties(cinemaDTO, cinema);

        cinema.setUpdateTime(LocalDateTime.now());
        cinema.setUpdateUser(BaseContext.getCurrentId());

        cinemaMapper.update(cinema);
    }

    @Override
    public void deleteById(Long id) {
        cinemaMapper.deleteById(id);
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        Cinema cinema = Cinema.builder()
                .id(id)
                .status(status)
                .updateTime(LocalDateTime.now())
                .updateUser(BaseContext.getCurrentId())
                .build();
        cinemaMapper.update(cinema);
    }

    @Override
    public CinemaVO getById(Long id) {
        Cinema cinema = cinemaMapper.getById(id);
        CinemaVO cinemaVO = new CinemaVO();
        BeanUtils.copyProperties(cinema, cinemaVO);
        return cinemaVO;
    }

    @Override
    public List<CinemaVO> list() {
        return cinemaMapper.list();
    }

    @Override
    public List<Cinema> getNearby() {
        return cinemaMapper.getNearby();
    }

    @Override
    public List<Cinema> listByStatus(Integer status) {
        return cinemaMapper.listByStatus(status);
    }

}
