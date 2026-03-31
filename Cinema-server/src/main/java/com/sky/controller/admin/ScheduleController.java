package com.sky.controller.admin;

import com.sky.dto.ScheduleDTO;
import com.sky.result.Result;
import com.sky.service.ScheduleService;
import com.sky.vo.ScheduleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController("adminScheduleController")
@RequestMapping("/admin/schedule")
@Api(tags = "排片管理")
@Slf4j
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping
    @ApiOperation("新增排片")
    public Result save(@RequestBody ScheduleDTO scheduleDTO) {
        log.info("新增排片：{}", scheduleDTO);
        scheduleService.save(scheduleDTO);
        return Result.success();
    }

    @PutMapping
    @ApiOperation("修改排片")
    public Result update(@RequestBody ScheduleDTO scheduleDTO) {
        log.info("修改排片：{}", scheduleDTO);
        scheduleService.update(scheduleDTO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除排片")
    public Result deleteById(@PathVariable Long id) {
        log.info("删除排片：{}", id);
        scheduleService.deleteById(id);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("排片启用禁用")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        log.info("排片启用禁用：{}, {}", status, id);
        scheduleService.startOrStop(status, id);
        return Result.success();
    }

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
    @ApiOperation("查询所有排片列表")
    public Result<List<ScheduleVO>> list() {
        log.info("查询所有排片列表");
        List<ScheduleVO> list = scheduleService.list();
        return Result.success(list);
    }

}