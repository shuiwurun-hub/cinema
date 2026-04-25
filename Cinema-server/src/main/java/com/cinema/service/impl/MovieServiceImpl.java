package com.cinema.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.cinema.constant.MessageConstant;
import com.cinema.constant.PasswordConstant;
import com.cinema.context.BaseContext;
import com.cinema.dto.MovieDTO;
import com.cinema.dto.MoviePageQueryDTO;
import com.cinema.entity.Category;
import com.cinema.entity.Movie;
import com.cinema.exception.DeletionNotAllowedException;
import com.cinema.mapper.CategoryMapper;
import com.cinema.mapper.MovieMapper;
import com.cinema.mapper.ScheduleMapper;
import com.cinema.result.PageResult;
import com.cinema.service.MovieService;
import com.cinema.vo.MovieVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Autowired
    private CategoryMapper categoryMapper;

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

        scheduleMapper.deleteByMovieId(id);

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
        if (movie == null) {
            return null;
        }
        MovieVO movieVO = new MovieVO();
        BeanUtils.copyProperties(movie, movieVO);
        
        if (movie.getCategoryId() != null) {
            Category category = categoryMapper.getById(movie.getCategoryId());
            if (category != null) {
                movieVO.setCategoryName(category.getName());
            }
        }
        
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
