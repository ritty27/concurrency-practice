package com.ritty27.dblock.repository

import com.ritty27.dblock.entity.AccountEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<AccountEntity, Long> {

}
