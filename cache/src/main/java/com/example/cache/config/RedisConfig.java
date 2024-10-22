package com.example.cache.config;

import com.example.cache.domain.entity.User;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    RedisTemplate<String, User> userRedisTemplate(RedisConnectionFactory connectionFactory) {
        var objectMapper = new ObjectMapper()
                //deserialize시 모르는값은 무시
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                //시간 타입 저장시에 설정해줄 것이 있음.
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);
        var template = new RedisTemplate<String, User>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        //redis에는 User 객체를 json으로 저장할거기에 이 시리얼라이져가 필요하다.
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(objectMapper, User.class));
        return template;
    }

    @Bean
    RedisTemplate<String, Object> objectRedisTemplate(RedisConnectionFactory connectionFactory) {
        PolymorphicTypeValidator validator = BasicPolymorphicTypeValidator
                .builder()
                .allowIfSubType(Object.class)
                .build();

        var objectMapper = new ObjectMapper()
                //deserialize시 모르는값은 무시
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                //시간 타입 저장시에 설정해줄 것이 있음.
                .registerModule(new JavaTimeModule())
                //프로젝트에 있는 클래스 정보를 저장하게 한다?
                .activateDefaultTyping(validator, ObjectMapper.DefaultTyping.NON_FINAL)
                .disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);
        var template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        //redis에는 User 객체를 json으로 저장할거기에 이 시리얼라이져가 필요하다.
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        return template;
    }
}
