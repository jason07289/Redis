package com.example.jediscache;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserRepository userRepository;
    private final JedisPool jedisPool;

    @GetMapping("/users/{id}/email")
    public String getUserEmail(@PathVariable Long id) {
        try(Jedis jedis = jedisPool.getResource()){
            var userEmailRedisKey = "users:id:email".formatted(id);
            //1. cashe 질의

            String userEmail = jedis.get(userEmailRedisKey);
            if(userEmail != null) {
                return userEmail;
            }
            //2. 없으면 db
            userEmail = userRepository.findById(id).orElse(User.builder().build()).getEmail();

            jedis.setex(userEmailRedisKey, 30, userEmail);
            return userEmail;
        }
    }
}
