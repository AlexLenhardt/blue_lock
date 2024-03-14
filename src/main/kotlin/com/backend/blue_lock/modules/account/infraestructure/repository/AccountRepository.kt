package com.backend.blue_lock.modules.account.infraestructure.repository

import com.backend.blue_lock.modules.account.domain.entities.Account
import java.util.UUID

interface AccountRepository {
    fun update(account: Account, userUUID: UUID)

    fun create(account: Account, userUUID: UUID)

    fun list(userUUID: UUID): List<Account>

    fun get(uuid: UUID, userUUID: UUID): Account?

    fun delete(uuid: UUID, userUUID: UUID)
}