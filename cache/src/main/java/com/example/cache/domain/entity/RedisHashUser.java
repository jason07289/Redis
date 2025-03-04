package com.example.cache.domain.entity;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;
@Builder
@Getter
@RedisHash(value = "redishash-user", timeToLive = 30L)
public class RedisHashUser {
    @Id
    private Long id;
    private String name;
    @Indexed
    private String email;
    private LocalDateTime createAt;
    private LocalDateTime updatedAt;

}
