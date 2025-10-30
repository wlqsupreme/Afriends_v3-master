package Afriends_v3.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Redis 通用服务
 * 提供最常用的 KV 读写与过期时间设置能力，便于业务方快速接入
 */
@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 写入一个键值对
     * @param key   键
     * @param value 值（会以 JSON 序列化）
     */
    public void set(String key, Object value) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(key, value);
    }

    /**
     * 写入一个带过期时间的键值对
     * @param key        键
     * @param value      值（会以 JSON 序列化）
     * @param ttlSeconds 过期时间（秒）
     */
    public void set(String key, Object value, long ttlSeconds) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(key, value, Duration.ofSeconds(ttlSeconds));
    }

    /**
     * 读取一个键
     * @param key 键
     * @return  值（自动反序列化为 Object）
     */
    public Object get(String key) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        return ops.get(key);
    }

    /**
     * 删除一个键
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 自增一个数值键（不存在则从 0 开始）
     */
    public long increment(String key, long delta) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        Long val = ops.increment(key, delta);
        return val == null ? 0L : val;
    }

    /**
     * 设置键的过期时间（秒）
     */
    public boolean expire(String key, long seconds) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, seconds, TimeUnit.SECONDS));
    }
}



