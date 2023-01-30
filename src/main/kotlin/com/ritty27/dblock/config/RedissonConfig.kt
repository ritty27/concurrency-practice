package com.ritty27.dblock.config

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RedissonConfig {

    @Bean
    fun redissonClient(): RedissonClient {
        return Redisson.create()
    }
}