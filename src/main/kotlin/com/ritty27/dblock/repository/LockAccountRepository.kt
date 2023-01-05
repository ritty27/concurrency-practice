package com.ritty27.dblock.repository

import com.ritty27.dblock.entity.AccountEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import javax.persistence.LockModeType

interface LockAccountRepository : JpaRepository<AccountEntity, Long> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query(
        "SELECT a FROM AccountEntity a WHERE a.id = :id"
    )
    fun findByIdWithLock(id: Long): AccountEntity?
}
