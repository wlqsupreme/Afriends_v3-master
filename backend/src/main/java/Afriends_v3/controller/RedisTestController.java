package Afriends_v3.controller;

import Afriends_v3.service.common.RedisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Redis 连通性自测接口
 * 访问 /api/redis/ping 可快速验证后端是否成功连接到 Redis
 */
@RestController
@RequestMapping("/api/redis")
public class RedisTestController {

    @Autowired
    private RedisService redisService;

    @GetMapping("/ping")
    public ResponseEntity<Map<String, Object>> ping() {
        Map<String, Object> resp = new HashMap<>();
        String key = "afriends:redis:pinger";
        try {
            redisService.set(key, "pong", 30);
            Object val = redisService.get(key);
            resp.put("ok", true);
            resp.put("key", key);
            resp.put("value", val);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            resp.put("ok", false);
            resp.put("message", "无法连接到 Redis，请确认 Redis 已启动并配置正确");
            resp.put("error", e.getClass().getSimpleName() + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(resp);
        }
    }
}



