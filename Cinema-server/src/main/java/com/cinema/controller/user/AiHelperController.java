package com.cinema.controller.user;

import com.cinema.service.ConsultantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;

@RestController("userAiHelperController")
@RequestMapping("/user/ai")
@Api(tags = "用户�?AI影评助手")
@Slf4j
@CrossOrigin(origins = "*")
public class AiHelperController {

    @Resource
    private ConsultantService consultantStreamingService;

    @RequestMapping(value = "/review", produces = "text/html;charset=utf-8")
    @ApiOperation("AI生成影评")
    public Flux<String> aiHelp(
            @RequestParam String memoryId,
            @RequestParam String message,
            @RequestParam(required = false) String[] imageList) {
        log.info("AI生成影评 - memoryId: {}, message: {}", memoryId, message);
        
        return consultantStreamingService.aiHelp(memoryId, message)
                .doOnError(e -> log.error("AI影评生成出错�?, e));
    }
}
