package com.ritty27.dblock.service

import com.ritty27.dblock.entity.AccountEntity
import com.ritty27.dblock.repository.AccountRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RedisLockAccountService(
    private val redisLockProvider: RedisLockProvider,
    private val accountRepository: AccountRepository
) {

    companion object {
        // Lock 재요청 대기 시간
        const val LOCK_RE_REQUEST_WAITE_TIME = 100
    }

    // FACADE
    fun decrease(id: Long, fare: Long): AccountEntity {
        // GET LOCK
        while (!redisLockProvider.getLock(id)) {
            Thread.sleep(LOCK_RE_REQUEST_WAITE_TIME.toLong())
        }

        // DATA FIX
        val account = decreaseFare(id, fare)

        // RELEASE LOCK
        redisLockProvider.releaseLock(id)

        return account
    }

    @Transactional
    fun decreaseFare(id: Long, fare: Long): AccountEntity {

        val account = accountRepository.findByIdOrNull(id) ?: throw RuntimeException()
        account.decreaseMoney(fare)

        accountRepository.save(account)

        return account
    }

}
