package com.cinema.controller.admin;

import com.cinema.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController("adminDashboardController")
@RequestMapping("/admin/dashboard")
@Api(tags = "数据统计")
@Slf4j
public class DashboardController {

    @GetMapping("/stats")
    @ApiOperation("获取统计数据")
    public Result<Map<String, Object>> getStats() {
        log.info("获取统计数据");
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("movieCount", 3);
        stats.put("cinemaCount", 3);
        stats.put("todayOrders", 5);
        stats.put("todayRevenue", 450.00);
        
        return Result.success(stats);
    }
}
