package com.cinema.controller.user;

import com.cinema.context.BaseContext;
import com.cinema.dto.RatingDTO;
import com.cinema.result.Result;
import com.cinema.service.RatingService;
import com.cinema.vo.RatingVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("userRatingController")
@RequestMapping("/user/rating")
@Api(tags = "用户�?评分")
@Slf4j
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @PostMapping
    @ApiOperation("提交评分")
    public Result<String> save(@RequestBody RatingDTO ratingDTO) {
        log.info("提交评分：{}", ratingDTO);
        
        RatingVO existingRating = ratingService.getByUserIdAndMovieId(
            BaseContext.getCurrentId(), 
            ratingDTO.getMovieId()
        );
        
        if (existingRating != null) {
            ratingService.update(ratingDTO);
            return Result.success("评分更新成功");
        } else {
            ratingService.save(ratingDTO);
            return Result.success("评分提交成功");
        }
    }

    @GetMapping("/{movieId}")
    @ApiOperation("根据电影id查询用户评分")
    public Result<RatingVO> getByMovieId(@PathVariable Long movieId) {
        log.info("根据电影id查询用户评分：{}", movieId);
        
        RatingVO ratingVO = ratingService.getByUserIdAndMovieId(
            BaseContext.getCurrentId(), 
            movieId
        );
        
        return Result.success(ratingVO);
    }

    @GetMapping("/list")
    @ApiOperation("查询用户所有评�?)
    public Result<List<RatingVO>> list() {
        log.info("查询用户所有评�?);
        
        List<RatingVO> list = ratingService.listByUserId(BaseContext.getCurrentId());
        
        return Result.success(list);
    }

    @GetMapping("/movie/{movieId}")
    @ApiOperation("查询电影所有评�?)
    public Result<List<RatingVO>> listByMovieId(@PathVariable Long movieId) {
        log.info("查询电影所有评分：{}", movieId);
        
        List<RatingVO> list = ratingService.listByMovieId(movieId);
        
        return Result.success(list);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除评分")
    public Result<String> deleteById(@PathVariable Long id) {
        log.info("删除评分：{}", id);
        
        ratingService.deleteById(id);
        
        return Result.success("删除成功");
    }
}
