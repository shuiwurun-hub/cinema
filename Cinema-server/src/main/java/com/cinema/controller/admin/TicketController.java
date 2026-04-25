package com.cinema.controller.admin;

import com.cinema.dto.TicketPageQueryDTO;
import com.cinema.result.PageResult;
import com.cinema.result.Result;
import com.cinema.service.TicketService;
import com.cinema.vo.TicketVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminTicketController")
@RequestMapping("/admin/ticket")
@Api(tags = "票务管理")
@Slf4j
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping("/{id}")
    @ApiOperation("根据id查询票务")
    public Result<TicketVO> getById(@PathVariable Long id) {
        log.info("根据id查询票务：{}", id);
        TicketVO ticketVO = ticketService.getById(id);
        return Result.success(ticketVO);
    }

    @GetMapping("/page")
    @ApiOperation("票务分页查询")
    public Result<PageResult> pageQuery(TicketPageQueryDTO ticketPageQueryDTO) {
        log.info("票务分页查询：{}", ticketPageQueryDTO);
        PageResult pageResult = ticketService.pageQuery(ticketPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/list")
    @ApiOperation("根据用户id查询票务列表")
    public Result<List<TicketVO>> listByUserId(Long userId) {
        log.info("根据用户id查询票务列表：{}", userId);
        List<TicketVO> list = ticketService.listByUserId(userId);
        return Result.success(list);
    }

}
