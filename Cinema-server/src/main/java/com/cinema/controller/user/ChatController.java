package com.cinema.controller.user;

import com.cinema.repository.RedisChatMemory;
import com.cinema.service.ConsultantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController("userChatController")
@RequestMapping("/user/chat")
@Api(tags = "用户�?AI聊天助手")
@Slf4j
@CrossOrigin(origins = "*")
public class ChatController {

    @Autowired
    private ConsultantService consultantStreamingService;

    @Autowired(required = false)
    private RedisChatMemory redisChatMemory;

    @RequestMapping(value = "/message", produces = "text/html;charset=utf-8")
    @ApiOperation("AI聊天对话")
    public Flux<String> chat(@RequestParam String memoryId, @RequestParam String message) {
        log.info("AI聊天对话 - memoryId: {}, message: {}", memoryId, message);

        StringBuilder aiResponse = new StringBuilder();
        return consultantStreamingService.chat(memoryId, message)
                .doOnNext(aiResponse::append)
                .doOnComplete(() -> {
                    try {
                        if (redisChatMemory != null) {
                            redisChatMemory.saveMessage(memoryId, message, aiResponse.toString());
                            log.info("AI回复已保存到Redis");
                        }
                    } catch (Exception e) {
                        log.warn("保存聊天历史失败（Redis未连接），但不影响AI聊天功能: {}", e.getMessage());
                    }
                })
                .doOnError(e -> log.error("AI流输出出错：", e));
    }

    @DeleteMapping("/history/{memoryId}")
    @ApiOperation("清空聊天历史")
    public void clearHistory(@PathVariable String memoryId) {
        log.info("清空聊天历史 - memoryId: {}", memoryId);
        try {
            if (redisChatMemory != null) {
                redisChatMemory.clear(memoryId);
            }
        } catch (Exception e) {
            log.warn("清空聊天历史失败（Redis未连接）: {}", e.getMessage());
        }
    }
}
