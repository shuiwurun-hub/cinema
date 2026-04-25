package com.cinema.mapper;

import com.cinema.entity.Seat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SeatMapper {

    void insert(Seat seat);

    void update(Seat seat);

    void deleteById(Long id);

    Seat getById(Long id);

    @Select("select * from seat where hall_id = #{hallId}")
    List<Seat> listByHallId(Long hallId);

    @Select("select * from seat where hall_id = #{hallId} and status = 1")
    List<Seat> listAvailableByHallId(Long hallId);

}
