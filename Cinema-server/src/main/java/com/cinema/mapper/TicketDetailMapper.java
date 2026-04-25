package com.cinema.mapper;

import com.cinema.entity.TicketDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TicketDetailMapper {

    void insert(TicketDetail ticketDetail);

    void deleteByTicketId(Long ticketId);

    @Select("select * from ticket_detail where ticket_id = #{ticketId}")
    List<TicketDetail> listByTicketId(Long ticketId);

    @Select("select td.seat_id from ticket_detail td " +
            "join ticket t on td.ticket_id = t.id " +
            "where t.schedule_id = #{scheduleId} and t.status = 2")
    List<Long> getSoldSeatIdsByScheduleId(Long scheduleId);

}
