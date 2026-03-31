package com.sky.mapper;

import com.sky.dto.TicketPageQueryDTO;
import com.sky.entity.Ticket;
import com.sky.vo.TicketVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TicketMapper {

    void insert(Ticket ticket);

    void update(Ticket ticket);

    void deleteById(Long id);

    Ticket getById(Long id);

    TicketVO getVOById(Long id);

    Page<TicketVO> pageQuery(TicketPageQueryDTO ticketPageQueryDTO);

    @Select("select * from ticket where user_id = #{userId} order by order_time desc")
    List<TicketVO> listByUserId(Long userId);

}