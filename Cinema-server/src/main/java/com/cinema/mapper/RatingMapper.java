package com.cinema.mapper;

import com.cinema.entity.Rating;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RatingMapper {

    void insert(Rating rating);

    void update(Rating rating);

    void deleteById(Long id);

    Rating getById(Long id);

    @Select("select * from rating where user_id = #{userId} and movie_id = #{movieId}")
    Rating getByUserIdAndMovieId(Long userId, Long movieId);

    @Select("select * from rating where user_id = #{userId} order by create_time desc")
    List<Rating> listByUserId(Long userId);

    @Select("select * from rating where movie_id = #{movieId} order by create_time desc")
    List<Rating> listByMovieId(Long movieId);
}
