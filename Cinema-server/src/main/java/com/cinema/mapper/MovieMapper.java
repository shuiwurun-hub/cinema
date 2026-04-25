package com.cinema.mapper;

import com.cinema.dto.MoviePageQueryDTO;
import com.cinema.entity.Movie;
import com.cinema.vo.MovieVO;
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

    @Select("<script>select * from movie <where> <if test='status != null'> status = #{status} </if> </where> </script>")
    List<Movie> list(Integer status);

}
