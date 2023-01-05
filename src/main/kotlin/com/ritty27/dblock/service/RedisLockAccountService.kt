package com.ritty27.dblock.service

import com.ritty27.dblock.entity.AccountEntity
import com.ritty27.dblock.repository.AccountRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class RedisLockAccountService(
    private val redisLockProvider: RedisLockProvider,
    private val accountRepository: AccountRepository
) {

    companion object {
        // Lock 재요청 대기 시간
        const val LOCK_RE_REQUEST_WAITE_TIME = 100
    }

    /**
     * TODO : 트랜잭션을 타면, lock release 이후 commit 이 진행되어 TEST 실패한다.
     * @Transactional
     */
//    @Transactional
    fun decrease(id: Long, fare: Long): AccountEntity {
        while (!redisLockProvider.getLock(id)) {
            Thread.sleep(LOCK_RE_REQUEST_WAITE_TIME.toLong())
        }

        val account = accountRepository.findByIdOrNull(id) ?: throw RuntimeException()
        account.decreaseMoney(fare)

        accountRepository.save(account)

        redisLockProvider.releaseLock(id)

        return account
    }

}
