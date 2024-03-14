package com.backend.blue_lock.modules.account.infraestructure.webservice

import com.backend.blue_lock.modules.account.domain.entities.Account
import com.backend.blue_lock.modules.account.domain.entities.AccountResponse
import com.backend.blue_lock.modules.account.domain.entities.ListAccountsResponse
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import com.backend.blue_lock.core.shared.entities.BasicFilter
import java.util.UUID

@Service
interface AccountWebService {
    fun save(account: Account): AccountResponse

    fun list(): ListAccountsResponse

    fun get(uuid: UUID): AccountResponse

    fun delete(uuid: UUID): AccountResponse
}