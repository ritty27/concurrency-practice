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
     * TODO
     * 1. Transaction 사용하지 않을 때만 성공
     * 2. Transaction 걸었을 때
     *      1) Redis Transaction 사용 -> Redis 는 Write Command 를 Transaction 종료 시점에 한 번에
     *      사용하기 때문에, Lock 을 얻을 수 없다. (테스트 하기 위해서는 Test 에서 Import RedisConfig 주석 제거 필요)
     *      2) Redis Transaction 미사용 -> Redis lock release 이후 DB commit 이 진행되어
     *      그 사이에 다른 곳에서 Lock 획득, 값 변경 할 수 있다. -> TEST 실패한다.
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
