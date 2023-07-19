package com.backend.blue_lock.modules.account.domain.usecase

import com.backend.blue_lock.modules.account.domain.entities.Account
import com.backend.blue_lock.modules.account.domain.entities.AccountResponse
import com.backend.blue_lock.core.shared.entities.BasicFilter
import com.backend.blue_lock.modules.account.domain.entities.ListAccountsResponse
import java.util.UUID

interface AccountUseCase {
    fun save(account: Account): AccountResponse

    fun list(): ListAccountsResponse

    fun get(uuid: UUID): AccountResponse
}