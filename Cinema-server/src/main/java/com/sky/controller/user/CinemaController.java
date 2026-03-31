package com.sky.controller.user;

import com.sky.entity.Cinema;
import com.sky.result.Result;
import com.sky.service.CinemaService;
import com.sky.vo.CinemaVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("userCinemaController")
@RequestMapping("/user/cinema")
@Api(tags = "用户端-影院")
@Slf4j
public class CinemaController {

    @Autowired
    private CinemaService cinemaService;

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