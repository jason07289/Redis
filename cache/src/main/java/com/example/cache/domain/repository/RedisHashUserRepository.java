package com.example.cache.domain.repository;

import com.example.cache.domain.entity.RedisHashUser;
import com.example.cache.domain.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface RedisHashUserRepository extends CrudRepository<RedisHashUser, Long> {

}
