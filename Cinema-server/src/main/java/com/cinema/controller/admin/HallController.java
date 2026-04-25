package com.cinema.controller.admin;

import com.cinema.dto.HallDTO;
import com.cinema.entity.Hall;
import com.cinema.result.Result;
import com.cinema.service.HallService;
import com.cinema.vo.HallVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminHallController")
@RequestMapping("/admin/hall")
@Api(tags = "影厅管理")
@Slf4j
public class HallController {

    @Autowired
    private HallService hallService;

    @PostMapping
    @ApiOperation("新增影厅")
    public Result save(@RequestBody HallDTO hallDTO) {
        log.info("新增影厅：{}", hallDTO);
        hallService.save(hallDTO);
        return Result.success();
    }

    @PutMapping
    @ApiOperation("修改影厅")
    public Result update(@RequestBody HallDTO hallDTO) {
        log.info("修改影厅：{}", hallDTO);
        hallService.update(hallDTO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除影厅")
    public Result deleteById(@PathVariable Long id) {
        log.info("删除影厅：{}", id);
        hallService.deleteById(id);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("影厅启用禁用")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        log.info("影厅启用禁用：{}, {}", status, id);
        hallService.startOrStop(status, id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询影厅")
    public Result<HallVO> getById(@PathVariable Long id) {
        log.info("根据id查询影厅：{}", id);
        HallVO hallVO = hallService.getById(id);
        return Result.success(hallVO);
    }

    @GetMapping("/list")
    @ApiOperation("查询影厅列表")
    public Result<List<Hall>> list(@RequestParam(required = false) Long cinemaId) {
        log.info("查询影厅列表：{}", cinemaId);
        List<Hall> list;
        if (cinemaId != null) {
            list = hallService.listByCinemaId(cinemaId);
        } else {
            list = hallService.listAll();
        }
        return Result.success(list);
    }

}
