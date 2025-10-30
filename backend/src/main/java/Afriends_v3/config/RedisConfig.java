package Afriends_v3.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 配置类
 * 目的：
 * 1) 提供一个通用的 RedisTemplate<String, Object>
 * 2) 统一 key 与 value 的序列化方式，避免出现乱码或类型无法反序列化的问题
 */
@Configuration
public class RedisConfig {

    /**
     * 配置一个通用的 RedisTemplate，key 使用字符串序列化，value 使用通用 JSON 序列化
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // key 与 hash key 采用字符串序列化，便于在命令行中直接可读
        StringRedisSerializer stringSerializer = new StringRedisSerializer();

        // value 与 hash value 采用通用 JSON 序列化，兼容绝大多数对象
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(customObjectMapper());

        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);

        template.afterPropertiesSet();
        return template;
    }

    /**
     * 自定义 ObjectMapper，开启更广泛的可见性，避免部分对象字段不可序列化
     */
    private ObjectMapper customObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        return mapper;
    }
}



