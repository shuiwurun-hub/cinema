package com.cinema.controller.user;

import com.cinema.dto.TicketSubmitDTO;
import com.cinema.result.Result;
import com.cinema.service.TicketService;
import com.cinema.vo.TicketVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("userTicketController")
@RequestMapping("/user/ticket")
@Api(tags = "用户�?票务")
@Slf4j
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/submit")
    @ApiOperation("提交购票订单")
    public Result<TicketVO> submit(@RequestBody TicketSubmitDTO ticketSubmitDTO) {
        log.info("提交购票订单：{}", ticketSubmitDTO);
        TicketVO ticketVO = ticketService.submit(ticketSubmitDTO);
        return Result.success(ticketVO);
    }

    @PostMapping("/pay/{id}")
    @ApiOperation("支付订单")
    public Result pay(@PathVariable Long id) {
        log.info("支付订单：{}", id);
        ticketService.pay(id);
        return Result.success();
    }

    @PostMapping("/cancel/{id}")
    @ApiOperation("取消订单")
    public Result cancel(@PathVariable Long id, @RequestParam String cancelReason) {
        log.info("取消订单：{}, 原因：{}", id, cancelReason);
        ticketService.cancel(id, cancelReason);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询票务")
    public Result<TicketVO> getById(@PathVariable Long id) {
        log.info("根据id查询票务：{}", id);
        TicketVO ticketVO = ticketService.getById(id);
        return Result.success(ticketVO);
    }

    @GetMapping("/list")
    @ApiOperation("查询我的订单")
    public Result<List<TicketVO>> list() {
        log.info("查询我的订单");
        List<TicketVO> list = ticketService.listByUserId(null);
        return Result.success(list);
    }

}
