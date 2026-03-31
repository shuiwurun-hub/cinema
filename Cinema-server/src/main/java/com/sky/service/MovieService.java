package com.sky.service;

import com.sky.dto.MovieDTO;
import com.sky.dto.MoviePageQueryDTO;
import com.sky.entity.Movie;
import com.sky.result.PageResult;
import com.sky.vo.MovieVO;

import java.util.List;

public interface MovieService {

    void save(MovieDTO movieDTO);

    void update(MovieDTO movieDTO);

    void deleteById(Long id);

    void startOrStop(Integer status, Long id);

    MovieVO getById(Long id);

    PageResult pageQuery(MoviePageQueryDTO moviePageQueryDTO);

    List<Movie> list(Integer status);

}