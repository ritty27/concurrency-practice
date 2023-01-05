package com.ritty27.dblock

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import java.sql.SQLException
import javax.sql.DataSource


@TestConfiguration
class RedisConfig(
    private val dataSource: DataSource
) {

    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory?): RedisTemplate<*, *> {
        val redisTemplate = RedisTemplate<ByteArray, ByteArray>()
        redisTemplate.setConnectionFactory(redisConnectionFactory!!)
        redisTemplate.setEnableTransactionSupport(true) // redis Transaction On !
        return redisTemplate
    }

    @Bean
    @Throws(SQLException::class)
    fun transactionManager(): PlatformTransactionManager {
        return DataSourceTransactionManager(dataSource)

    }
}
