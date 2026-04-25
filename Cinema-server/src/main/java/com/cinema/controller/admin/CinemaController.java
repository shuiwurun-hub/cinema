package com.cinema.controller.admin;

import com.cinema.dto.CinemaDTO;
import com.cinema.entity.Cinema;
import com.cinema.result.Result;
import com.cinema.service.CinemaService;
import com.cinema.vo.CinemaVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminCinemaController")
@RequestMapping("/admin/cinema")
@Api(tags = "影院管理")
@Slf4j
public class CinemaController {

    @Autowired
    private CinemaService cinemaService;

    @PostMapping
    @ApiOperation("新增影院")
    public Result save(@RequestBody CinemaDTO cinemaDTO) {
        log.info("新增影院：{}", cinemaDTO);
        cinemaService.save(cinemaDTO);
        return Result.success();
    }

    @PutMapping
    @ApiOperation("修改影院")
    public Result update(@RequestBody CinemaDTO cinemaDTO) {
        log.info("修改影院：{}", cinemaDTO);
        cinemaService.update(cinemaDTO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除影院")
    public Result deleteById(@PathVariable Long id) {
        log.info("删除影院：{}", id);
        cinemaService.deleteById(id);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("影院启用禁用")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        log.info("影院启用禁用：{}, {}", status, id);
        cinemaService.startOrStop(status, id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询影院")
    public Result<CinemaVO> getById(@PathVariable Long id) {
        log.info("根据id查询影院：{}", id);
        CinemaVO cinemaVO = cinemaService.getById(id);
        return Result.success(cinemaVO);
    }

    @GetMapping("/list")
    @ApiOperation("影院列表查询")
    public Result<List<CinemaVO>> list() {
        log.info("影院列表查询");
        List<CinemaVO> list = cinemaService.list();
        return Result.success(list);
    }

}
