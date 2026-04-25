package com.cinema.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.cinema.context.BaseContext;
import com.cinema.dto.TicketPageQueryDTO;
import com.cinema.dto.TicketSubmitDTO;
import com.cinema.entity.Schedule;
import com.cinema.entity.Seat;
import com.cinema.entity.Ticket;
import com.cinema.entity.TicketDetail;
import com.cinema.entity.User;
import com.cinema.exception.SeatNotAvailableException;
import com.cinema.exception.TicketBusinessException;
import com.cinema.mapper.ScheduleMapper;
import com.cinema.mapper.SeatMapper;
import com.cinema.mapper.TicketDetailMapper;
import com.cinema.mapper.TicketMapper;
import com.cinema.mapper.UserMapper;
import com.cinema.result.PageResult;
import com.cinema.service.TicketService;
import com.cinema.vo.TicketVO;
import com.cinema.vo.TicketDetailVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private TicketDetailMapper ticketDetailMapper;

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Autowired
    private SeatMapper seatMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public TicketVO submit(TicketSubmitDTO ticketSubmitDTO) {
        Long userId = BaseContext.getCurrentId();
        Long scheduleId = ticketSubmitDTO.getScheduleId();
        List<Long> seatIds = ticketSubmitDTO.getSeatIds();

        Schedule schedule = scheduleMapper.getById(scheduleId);
        if (schedule == null) {
            throw new TicketBusinessException("排片不存�?);
        }

        BigDecimal totalAmount = schedule.getPrice().multiply(new BigDecimal(seatIds.size()));

        List<Long> soldSeatIds = ticketDetailMapper.getSoldSeatIdsByScheduleId(scheduleId);
        for (Long seatId : seatIds) {
            if (soldSeatIds.contains(seatId)) {
                throw new SeatNotAvailableException("座位已被购买，请重新选择");
            }
            
            Seat seat = seatMapper.getById(seatId);
            if (seat == null || seat.getStatus() != 1) {
                throw new SeatNotAvailableException("座位不可�?);
            }
        }

        String orderNumber = generateOrderNumber();

        Ticket ticket = Ticket.builder()
                .number(orderNumber)
                .status(Ticket.PAID)
                .userId(userId)
                .scheduleId(scheduleId)
                .orderTime(LocalDateTime.now())
                .payTime(LocalDateTime.now())
                .payMethod(ticketSubmitDTO.getPayMethod())
                .payStatus(Ticket.PAID_STATUS)
                .amount(totalAmount)
                .remark(ticketSubmitDTO.getRemark())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        User user = userMapper.getById(userId);
        if (user != null) {
            ticket.setUserName(user.getName());
            ticket.setPhone(user.getPhone());
        }

        ticketMapper.insert(ticket);

        for (Long seatId : seatIds) {
            Seat seat = seatMapper.getById(seatId);
            TicketDetail detail = TicketDetail.builder()
                    .ticketId(ticket.getId())
                    .seatId(seatId)
                    .price(schedule.getPrice())
                    .rowNum(seat.getRowNum())
                    .colNum(seat.getColNum())
                    .seatType(seat.getSeatType())
                    .build();
            ticketDetailMapper.insert(detail);
        }

        return ticketMapper.getVOById(ticket.getId());
    }

    @Override
    @Transactional
    public void pay(Long id) {
        Ticket ticket = ticketMapper.getById(id);
        if (ticket == null) {
            throw new TicketBusinessException("订单不存�?);
        }

        if (ticket.getStatus() != Ticket.PENDING_PAYMENT) {
            throw new TicketBusinessException("订单状态异�?);
        }

        ticket.setStatus(Ticket.PAID);
        ticket.setPayStatus(Ticket.PAID_STATUS);
        ticket.setPayTime(LocalDateTime.now());
        ticket.setUpdateTime(LocalDateTime.now());

        ticketMapper.update(ticket);
    }

    @Override
    @Transactional
    public void cancel(Long id, String cancelReason) {
        Ticket ticket = ticketMapper.getById(id);
        if (ticket == null) {
            throw new TicketBusinessException("订单不存�?);
        }

        if (ticket.getStatus() == Ticket.PAID) {
            throw new TicketBusinessException("已支付订单无法取�?);
        }

        ticket.setStatus(Ticket.CANCELLED);
        ticket.setCancelReason(cancelReason);
        ticket.setCancelTime(LocalDateTime.now());
        ticket.setUpdateTime(LocalDateTime.now());

        ticketMapper.update(ticket);
    }

    @Override
    public TicketVO getById(Long id) {
        return ticketMapper.getVOById(id);
    }

    @Override
    public PageResult pageQuery(TicketPageQueryDTO ticketPageQueryDTO) {
        PageHelper.startPage(ticketPageQueryDTO.getPage(), ticketPageQueryDTO.getPageSize());
        Page<TicketVO> page = ticketMapper.pageQuery(ticketPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public List<TicketVO> listByUserId(Long userId) {
        if (userId == null) {
            userId = BaseContext.getCurrentId();
        }
        List<TicketVO> tickets = ticketMapper.listByUserId(userId);
        
        for (TicketVO ticket : tickets) {
            List<TicketDetail> details = ticketDetailMapper.listByTicketId(ticket.getId());
            List<TicketDetailVO> detailVOs = new ArrayList<>();
            for (TicketDetail detail : details) {
                TicketDetailVO detailVO = new TicketDetailVO();
                BeanUtils.copyProperties(detail, detailVO);
                detailVOs.add(detailVO);
            }
            ticket.setTicketDetails(detailVOs);
        }
        
        return tickets;
    }

    private String generateOrderNumber() {
        return "T" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

}
