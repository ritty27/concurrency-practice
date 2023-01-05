package com.ritty27.dblock.service

import com.ritty27.dblock.entity.AccountEntity
import com.ritty27.dblock.repository.LockAccountRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


@SpringBootTest
class PessimisticLockAccountServiceTest(
) {
    @Autowired
    lateinit var pessimisticLockAccountService: PessimisticLockAccountService

    @Autowired
    lateinit var lockAccountRepository: LockAccountRepository


    @BeforeEach
    fun insert() {
        val account = AccountEntity(1L, 1000L)
        lockAccountRepository.save(account)
    }

    @AfterEach
    fun delete() {
        val account: AccountEntity = lockAccountRepository.findByIdOrNull(1L) ?: throw RuntimeException()

        println("TEST 이후 Money = ${account.money}")
        account.money = 1_000
        lockAccountRepository.save(account)
    }

    @Test
    fun pessimisticLockTest() {
        val threadCount = 100
        val executorService: ExecutorService = Executors.newFixedThreadPool(20)
        val latch = CountDownLatch(threadCount)
        for (i in 0 until threadCount) {
            executorService.submit {
                try {
                    pessimisticLockAccountService.decrease(1L, 10L)
                } catch (e: Exception) {
                    println(e)
                } finally {
                    latch.countDown()
                }
            }
        }
        latch.await()

        val account = lockAccountRepository.findByIdOrNull(1L) ?: throw RuntimeException()

        assertEquals(0, account.money)
    }


}
