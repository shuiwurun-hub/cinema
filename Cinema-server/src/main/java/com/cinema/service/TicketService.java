package com.cinema.service;

import com.cinema.dto.TicketPageQueryDTO;
import com.cinema.dto.TicketSubmitDTO;
import com.cinema.result.PageResult;
import com.cinema.vo.TicketVO;

import java.util.List;

public interface TicketService {

    TicketVO submit(TicketSubmitDTO ticketSubmitDTO);

    void pay(Long id);

    void cancel(Long id, String cancelReason);

    TicketVO getById(Long id);

    PageResult pageQuery(TicketPageQueryDTO ticketPageQueryDTO);

    List<TicketVO> listByUserId(Long userId);

}
