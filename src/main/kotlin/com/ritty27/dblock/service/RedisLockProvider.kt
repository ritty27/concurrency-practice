package com.ritty27.dblock.service

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class RedisLockProvider(
    private val redisTemplate: RedisTemplate<String, String>
) {

    private val opsForValue = redisTemplate.opsForValue()

    fun getLock(key: Long): Boolean {
        return opsForValue
            .setIfAbsent(generateKey(key), "lock", Duration.ofSeconds(3))?: false
    }

    fun releaseLock(key: Long) {
        redisTemplate.delete(generateKey(key))
    }

    fun generateKey(key: Long): String {
        return "accountLock:$key"
    }

}
