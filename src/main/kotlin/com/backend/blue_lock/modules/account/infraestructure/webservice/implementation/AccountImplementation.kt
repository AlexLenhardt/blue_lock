package com.backend.blue_lock.modules.account.infraestructure.webservice.implementation

import com.backend.blue_lock.modules.account.domain.entities.Account
import com.backend.blue_lock.modules.account.domain.entities.AccountResponse
import com.backend.blue_lock.modules.account.domain.entities.ListAccountsResponse
import com.backend.blue_lock.modules.account.domain.usecase.AccountUseCase
import com.backend.blue_lock.modules.account.infraestructure.webservice.AccountWebService
import org.springframework.web.bind.annotation.*
import com.backend.blue_lock.core.shared.entities.BasicFilter
import java.util.UUID

@RestController
@RequestMapping("/account")
class AccountWebServiceImplemetation(
    val usecase: AccountUseCase
) : AccountWebService {
    
    @PostMapping
    override fun save(@RequestBody account: Account): AccountResponse {
        return usecase.save(account)
    }

    @GetMapping
    override fun list(): ListAccountsResponse {
        return usecase.list()
    }

    @GetMapping("/{uuid}")
    override fun get(
        @PathVariable uuid: UUID
    ): AccountResponse {
        return usecase.get(uuid)
    }
}
