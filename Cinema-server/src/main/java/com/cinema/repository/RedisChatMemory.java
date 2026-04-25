package com.cinema.repository;

import cn.hutool.json.JSONUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public class RedisChatMemory {
    
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final String PREFIX = "ai:chat:";

    public List<String> getHistory(String sessionId) {
        String json = stringRedisTemplate.opsForValue().get(PREFIX + sessionId);
        if (json == null || json.isEmpty()) return new ArrayList<>();
        return JSONUtil.toList(json, String.class);
    }

    public void saveMessage(String sessionId, String userMessage, String aiMessage) {
        List<String> history = getHistory(sessionId);
        history.add("User: " + userMessage);
        history.add("AI: " + aiMessage);

        if (history.size() > 40) {
            history = history.subList(history.size() - 40, history.size());
        }

        stringRedisTemplate.opsForValue().set(
                PREFIX + sessionId,
                JSONUtil.toJsonStr(history),
                Duration.ofDays(1)
        );
    }

    public void clear(String sessionId) {
        stringRedisTemplate.delete(PREFIX + sessionId);
    }
}
