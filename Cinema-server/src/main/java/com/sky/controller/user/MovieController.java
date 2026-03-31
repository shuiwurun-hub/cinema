package com.sky.controller.user;

import com.sky.entity.Movie;
import com.sky.result.Result;
import com.sky.service.MovieService;
import com.sky.vo.MovieVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("userMovieController")
@RequestMapping("/user/movie")
@Api(tags = "用户端-电影")
@Slf4j
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/{id}")
    @ApiOperation("根据id查询电影")
    public Result<MovieVO> getById(@PathVariable Long id) {
        log.info("根据id查询电影：{}", id);
        MovieVO movieVO = movieService.getById(id);
        return Result.success(movieVO);
    }

    @GetMapping("/list")
    @ApiOperation("电影列表查询")
    public Result<List<Movie>> list(@RequestParam(required = false) Integer status) {
        log.info("电影列表查询：{}", status);
        List<Movie> list = movieService.list(status);
        return Result.success(list);
    }

}