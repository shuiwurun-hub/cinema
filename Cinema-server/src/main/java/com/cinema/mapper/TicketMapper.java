package com.cinema.mapper;

import com.cinema.dto.TicketPageQueryDTO;
import com.cinema.entity.Ticket;
import com.cinema.vo.TicketVO;
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

    List<TicketVO> listByUserId(Long userId);

}
