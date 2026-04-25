package com.cinema.service;

import com.cinema.dto.MovieDTO;
import com.cinema.dto.MoviePageQueryDTO;
import com.cinema.entity.Movie;
import com.cinema.result.PageResult;
import com.cinema.vo.MovieVO;

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
