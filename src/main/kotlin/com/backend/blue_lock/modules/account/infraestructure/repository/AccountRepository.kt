package com.backend.blue_lock.modules.account.infraestructure.repository

import com.backend.blue_lock.modules.account.domain.entities.Account
import java.util.UUID

interface AccountRepository {
    fun update(account: Account)

    fun create(account: Account)

    fun list(): List<Account>

    fun get(uuid: UUID): Account?
}