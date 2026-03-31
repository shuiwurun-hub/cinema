package com.sky.service;

import com.sky.dto.TicketPageQueryDTO;
import com.sky.dto.TicketSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.TicketVO;

import java.util.List;

public interface TicketService {

    TicketVO submit(TicketSubmitDTO ticketSubmitDTO);

    void pay(Long id);

    void cancel(Long id, String cancelReason);

    TicketVO getById(Long id);

    PageResult pageQuery(TicketPageQueryDTO ticketPageQueryDTO);

    List<TicketVO> listByUserId(Long userId);

}