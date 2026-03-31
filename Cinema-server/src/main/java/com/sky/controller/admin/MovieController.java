package com.sky.controller.admin;

import com.sky.dto.MovieDTO;
import com.sky.dto.MoviePageQueryDTO;
import com.sky.entity.Movie;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.MovieService;
import com.sky.vo.MovieVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminMovieController")
@RequestMapping("/admin/movie")
@Api(tags = "电影管理")
@Slf4j
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping
    @ApiOperation("新增电影")
    public Result save(@RequestBody MovieDTO movieDTO) {
        log.info("新增电影：{}", movieDTO);
        movieService.save(movieDTO);
        return Result.success();
    }

    @PutMapping
    @ApiOperation("修改电影")
    public Result update(@RequestBody MovieDTO movieDTO) {
        log.info("修改电影：{}", movieDTO);
        movieService.update(movieDTO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除电影")
    public Result deleteById(@PathVariable Long id) {
        log.info("删除电影：{}", id);
        movieService.deleteById(id);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("电影起售停售")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        log.info("电影起售停售：{}, {}", status, id);
        movieService.startOrStop(status, id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询电影")
    public Result<MovieVO> getById(@PathVariable Long id) {
        log.info("根据id查询电影：{}", id);
        MovieVO movieVO = movieService.getById(id);
        return Result.success(movieVO);
    }

    @GetMapping("/page")
    @ApiOperation("电影分页查询")
    public Result<PageResult> page(MoviePageQueryDTO moviePageQueryDTO) {
        log.info("电影分页查询：{}", moviePageQueryDTO);
        PageResult pageResult = movieService.pageQuery(moviePageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/list")
    @ApiOperation("电影列表查询")
    public Result<List<Movie>> list(@RequestParam(required = false) Integer status) {
        log.info("电影列表查询：{}", status);
        List<Movie> list = movieService.list(status);
        return Result.success(list);
    }

}