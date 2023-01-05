package com.ritty27.dblock.service

import com.ritty27.dblock.entity.AccountEntity
import com.ritty27.dblock.repository.LockAccountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PessimisticLockAccountService(
    private val lockAccountRepository: LockAccountRepository
) {

    @Transactional
    fun decrease(id: Long, fare: Long): AccountEntity {
        val account = lockAccountRepository.findByIdWithLock(id) ?: throw RuntimeException()
        account.decreaseMoney(fare)

        lockAccountRepository.save(account)

        return account
    }

}
