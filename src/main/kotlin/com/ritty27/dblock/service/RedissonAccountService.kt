package com.ritty27.dblock.service

import com.ritty27.dblock.entity.AccountEntity
import com.ritty27.dblock.repository.AccountRepository
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

@Service
class RedissonAccountService(
    private val redissonClient: RedissonClient,
    private val accountRepository: AccountRepository
) {

    companion object {
        // Lock 재요청 대기 시간
        const val LOCK_RE_REQUEST_WAITE_TIME = 10
    }


    fun decreaseFacade(id: Long, fare: Long): AccountEntity? {
        val lock: RLock = redissonClient.getLock(id.toString())

        try {
            val isAvailable = lock.tryLock(LOCK_RE_REQUEST_WAITE_TIME.toLong(), 1, TimeUnit.SECONDS)
            if (!isAvailable) {
                println("redisson getLock timeout")
                return null
            }
            return decrease(id, fare)
        } finally {
            lock.unlock()
        }

    }

    @Transactional
    fun decrease(id: Long, fare: Long): AccountEntity {

        val account = accountRepository.findByIdOrNull(id) ?: throw RuntimeException()
        account.decreaseMoney(fare)

        accountRepository.save(account)

        return account
    }
}
