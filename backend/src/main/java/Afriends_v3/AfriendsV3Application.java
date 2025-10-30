package Afriends_v3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching // 开启 Spring 缓存注解能力（@Cacheable/@CacheEvict 等）
public class AfriendsV3Application {

    public static void main(String[] args) {
        SpringApplication.run(AfriendsV3Application.class, args);
    }

}
