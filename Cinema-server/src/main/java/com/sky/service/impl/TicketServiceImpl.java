package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.context.BaseContext;
import com.sky.dto.TicketPageQueryDTO;
import com.sky.dto.TicketSubmitDTO;
import com.sky.entity.Schedule;
import com.sky.entity.Seat;
import com.sky.entity.Ticket;
import com.sky.entity.TicketDetail;
import com.sky.entity.User;
import com.sky.exception.SeatNotAvailableException;
import com.sky.exception.TicketBusinessException;
import com.sky.mapper.ScheduleMapper;
import com.sky.mapper.SeatMapper;
import com.sky.mapper.TicketDetailMapper;
import com.sky.mapper.TicketMapper;
import com.sky.mapper.UserMapper;
import com.sky.result.PageResult;
import com.sky.service.TicketService;
import com.sky.vo.TicketVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
            throw new TicketBusinessException("排片不存在");
        }

        BigDecimal totalAmount = schedule.getPrice().multiply(new BigDecimal(seatIds.size()));

        for (Long seatId : seatIds) {
            Seat seat = seatMapper.getById(seatId);
            if (seat == null || seat.getStatus() != 1) {
                throw new SeatNotAvailableException("座位不可用");
            }
        }

        String orderNumber = generateOrderNumber();

        Ticket ticket = Ticket.builder()
                .number(orderNumber)
                .status(Ticket.PENDING_PAYMENT)
                .userId(userId)
                .scheduleId(scheduleId)
                .orderTime(LocalDateTime.now())
                .payMethod(ticketSubmitDTO.getPayMethod())
                .payStatus(Ticket.UN_PAID)
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
            throw new TicketBusinessException("订单不存在");
        }

        if (ticket.getStatus() != Ticket.PENDING_PAYMENT) {
            throw new TicketBusinessException("订单状态异常");
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
            throw new TicketBusinessException("订单不存在");
        }

        if (ticket.getStatus() == Ticket.PAID) {
            throw new TicketBusinessException("已支付订单无法取消");
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
        return ticketMapper.listByUserId(userId);
    }

    private String generateOrderNumber() {
        return "T" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

}