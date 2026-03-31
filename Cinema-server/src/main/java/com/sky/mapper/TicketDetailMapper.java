package com.sky.mapper;

import com.sky.entity.TicketDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TicketDetailMapper {

    void insert(TicketDetail ticketDetail);

    void deleteByTicketId(Long ticketId);

    @Select("select * from ticket_detail where ticket_id = #{ticketId}")
    List<TicketDetail> listByTicketId(Long ticketId);

}