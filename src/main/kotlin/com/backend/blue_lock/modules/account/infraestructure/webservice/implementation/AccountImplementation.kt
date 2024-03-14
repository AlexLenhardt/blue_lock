package com.backend.blue_lock.modules.account.infraestructure.webservice.implementation

import com.backend.blue_lock.modules.account.domain.entities.Account
import com.backend.blue_lock.modules.account.domain.entities.AccountResponse
import com.backend.blue_lock.modules.account.domain.entities.ListAccountsResponse
import com.backend.blue_lock.modules.account.domain.usecase.AccountUseCase
import com.backend.blue_lock.modules.account.infraestructure.webservice.AccountWebService
import org.springframework.web.bind.annotation.*
import com.backend.blue_lock.core.shared.entities.BasicFilter
import java.util.UUID
import org.springframework.security.core.context.SecurityContextHolder
import com.backend.blue_lock.core.user.security.SystemUser

@RestController
@RequestMapping("/account")
class AccountWebServiceImplemetation(
    val usecase: AccountUseCase
) : AccountWebService {
    
    @PostMapping
    override fun save(@RequestBody account: Account): AccountResponse {
        val user = SecurityContextHolder.getContext().authentication.principal as SystemUser

        return usecase.save(account, user.getUserData())
    }

    @GetMapping
    override fun list(): ListAccountsResponse {
        val user = SecurityContextHolder.getContext().authentication.principal as SystemUser

        return usecase.list(user.getUserData())
    }

    @GetMapping("/{uuid}")
    override fun get(
        @PathVariable uuid: UUID
    ): AccountResponse {
        val user = SecurityContextHolder.getContext().authentication.principal as SystemUser
    
        return usecase.get(uuid, user.getUserData())
    }

    @DeleteMapping("/{uuid}")
    override fun delete(
        @PathVariable uuid: UUID
    ): AccountResponse {
        val user = SecurityContextHolder.getContext().authentication.principal as SystemUser
    
        return usecase.delete(uuid, user.getUserData())
    }
}
