package com.cinema.controller.user;

import com.cinema.result.Result;
import com.cinema.service.ScheduleService;
import com.cinema.vo.ScheduleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController("userScheduleController")
@RequestMapping("/user/schedule")
@Api(tags = "用户�?排片")
@Slf4j
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/{id}")
    @ApiOperation("根据id查询排片")
    public Result<ScheduleVO> getById(@PathVariable Long id) {
        log.info("根据id查询排片：{}", id);
        ScheduleVO scheduleVO = scheduleService.getById(id);
        return Result.success(scheduleVO);
    }

    @GetMapping("/list/movie/{movieId}")
    @ApiOperation("根据电影id查询排片列表")
    public Result<List<ScheduleVO>> listByMovieId(@PathVariable Long movieId,
                                                 @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        log.info("根据电影id查询排片列表：{}, {}", movieId, date);
        List<ScheduleVO> list = scheduleService.listByMovieId(movieId, date);
        return Result.success(list);
    }

    @GetMapping("/list/cinema/{cinemaId}")
    @ApiOperation("根据影院id查询排片列表")
    public Result<List<ScheduleVO>> listByCinemaId(@PathVariable Long cinemaId,
                                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        log.info("根据影院id查询排片列表：{}, {}", cinemaId, date);
        List<ScheduleVO> list = scheduleService.listByCinemaId(cinemaId, date);
        return Result.success(list);
    }

    @GetMapping("/list")
    @ApiOperation("查询排片列表（支持筛选）")
    public Result<List<ScheduleVO>> list(
            @RequestParam(required = false) Long movieId,
            @RequestParam(required = false) Long cinemaId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate showDate) {
        log.info("查询排片列表，筛选条件：movieId={}, cinemaId={}, showDate={}", movieId, cinemaId, showDate);
        
        List<ScheduleVO> list;
        if (movieId == null && cinemaId == null && showDate == null) {
            list = scheduleService.list();
        } else {
            list = scheduleService.listWithFilters(movieId, cinemaId, showDate);
        }
        
        return Result.success(list);
    }

}
