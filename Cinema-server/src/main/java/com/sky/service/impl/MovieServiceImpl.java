package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.context.BaseContext;
import com.sky.dto.MovieDTO;
import com.sky.dto.MoviePageQueryDTO;
import com.sky.entity.Movie;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.MovieMapper;
import com.sky.result.PageResult;
import com.sky.service.MovieService;
import com.sky.vo.MovieVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieMapper movieMapper;

    @Override
    public void save(MovieDTO movieDTO) {
        Movie movie = new Movie();
        BeanUtils.copyProperties(movieDTO, movie);

        movie.setStatus(1);

        movie.setCreateTime(LocalDateTime.now());
        movie.setUpdateTime(LocalDateTime.now());
        movie.setCreateUser(BaseContext.getCurrentId());
        movie.setUpdateUser(BaseContext.getCurrentId());

        movieMapper.insert(movie);
    }

    @Override
    public void update(MovieDTO movieDTO) {
        Movie movie = new Movie();
        BeanUtils.copyProperties(movieDTO, movie);

        movie.setUpdateTime(LocalDateTime.now());
        movie.setUpdateUser(BaseContext.getCurrentId());

        movieMapper.update(movie);
    }

    @Override
    public void deleteById(Long id) {
        Movie movie = movieMapper.getById(id);
        if (movie == null) {
            throw new DeletionNotAllowedException(MessageConstant.MOVICE_NOT_FOUND);
        }

        movieMapper.deleteById(id);
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        Movie movie = Movie.builder()
                .id(id)
                .status(status)
                .updateTime(LocalDateTime.now())
                .updateUser(BaseContext.getCurrentId())
                .build();
        movieMapper.update(movie);
    }

    @Override
    public MovieVO getById(Long id) {
        Movie movie = movieMapper.getById(id);
        MovieVO movieVO = new MovieVO();
        BeanUtils.copyProperties(movie, movieVO);
        return movieVO;
    }

    @Override
    public PageResult pageQuery(MoviePageQueryDTO moviePageQueryDTO) {
        PageHelper.startPage(moviePageQueryDTO.getPage(), moviePageQueryDTO.getPageSize());
        Page<MovieVO> page = movieMapper.pageQuery(moviePageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public List<Movie> list(Integer status) {
        return movieMapper.list(status);
    }

}