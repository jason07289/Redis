package com.example.jediscache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {
    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10); // 최대 커넥션 수 설정
        poolConfig.setMaxIdle(5);   // 유휴 상태의 최대 커넥션 수
        poolConfig.setMinIdle(1);   // 유휴 상태의 최소 커넥션 수
        poolConfig.setTestOnBorrow(true); // 빌릴 때 커넥션 테스트 여부
        poolConfig.setJmxEnabled(false);

        return poolConfig;
    }

    @Bean
    public JedisPool jedisPool(JedisPoolConfig jedisPoolConfig) {
        // "localhost"는 Redis 서버 주소, 6379는 포트 번호
        return new JedisPool(jedisPoolConfig, "localhost", 6379);
    }
}
