package com.sky.mapper;

import com.sky.dto.MoviePageQueryDTO;
import com.sky.entity.Movie;
import com.sky.vo.MovieVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MovieMapper {

    void insert(Movie movie);

    void update(Movie movie);

    void deleteById(Long id);

    Movie getById(Long id);

    Page<MovieVO> pageQuery(MoviePageQueryDTO moviePageQueryDTO);

    @Select("select * from movie where status = #{status}")
    List<Movie> list(Integer status);

}