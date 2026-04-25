package com.cinema.controller.admin;

import com.cinema.entity.Seat;
import com.cinema.result.Result;
import com.cinema.service.SeatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminSeatController")
@RequestMapping("/admin/seat")
@Api(tags = "座位管理")
@Slf4j
public class SeatController {

    @Autowired
    private SeatService seatService;

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

}
