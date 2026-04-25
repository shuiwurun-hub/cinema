package com.cinema.controller.user;

import com.cinema.entity.Seat;
import com.cinema.mapper.TicketDetailMapper;
import com.cinema.result.Result;
import com.cinema.service.SeatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("userSeatController")
@RequestMapping("/user/seat")
@Api(tags = "用户�?座位")
@Slf4j
public class SeatController {

    @Autowired
    private SeatService seatService;

    @Autowired
    private TicketDetailMapper ticketDetailMapper;

    @GetMapping("/{id}")
    @ApiOperation("根据id查询座位")
    public Result<Seat> getById(@PathVariable Long id) {
        log.info("根据id查询座位：{}", id);
        Seat seat = seatService.getById(id);
        return Result.success(seat);
    }

    @GetMapping("/list/{hallId}")
    @ApiOperation("根据影厅id查询座位列表")
    public Result<List<Seat>> listByHallId(@PathVariable Long hallId) {
        log.info("根据影厅id查询座位列表：{}", hallId);
        List<Seat> list = seatService.listByHallId(hallId);
        return Result.success(list);
    }

    @GetMapping("/sold/{scheduleId}")
    @ApiOperation("查询某场次已售座位ID列表")
    public Result<List<Long>> getSoldSeatIds(@PathVariable Long scheduleId) {
        log.info("查询某场次已售座位ID列表：{}", scheduleId);
        List<Long> soldSeatIds = ticketDetailMapper.getSoldSeatIdsByScheduleId(scheduleId);
        log.info("已售座位ID列表：{}", soldSeatIds);
        return Result.success(soldSeatIds);
    }

}
