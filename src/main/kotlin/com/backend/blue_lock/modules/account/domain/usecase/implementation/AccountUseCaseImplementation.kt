package com.backend.blue_lock.modules.account.domain.usecase.implementation

import com.backend.blue_lock.modules.account.domain.usecase.AccountUseCase
import com.backend.blue_lock.modules.account.domain.entities.Account
import com.backend.blue_lock.modules.account.domain.entities.AccountResponse
import com.backend.blue_lock.modules.account.domain.exception.*
import com.backend.blue_lock.modules.account.infraestructure.repository.AccountRepository
import com.github.f4b6a3.uuid.UuidCreator
import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory
import com.backend.blue_lock.modules.account.domain.entities.ListAccountsResponse
import com.backend.blue_lock.core.shared.entities.BasicFilter
import com.backend.blue_lock.core.user.domain.entities.User
import java.util.UUID

@Service
class AccountUseCaseImplementation(
    val repository: AccountRepository
):AccountUseCase {
    companion object {
        private val logger = LoggerFactory.getLogger(AccountUseCaseImplementation::class.java)
    }

    override fun save(account: Account, user: User): AccountResponse {
        if (account.label.isNullOrBlank()) {
            return AccountResponse(error = ACCOUNT_LABEL_EMPTY)
        }

        return try {
            if (account.uuid != null){
                repository.update(account, user.uuid!!)
            }else{
                account.uuid = UuidCreator.getTimeOrdered()
                repository.create(account, user.uuid!!)
            }

            AccountResponse(account = account)
        }catch(e: Exception){
            logger.error("ERROR DURING SAVE ACCOUNT", e)
            return AccountResponse(error = POST_ACCOUNT_ERROR)
        }
    }

    override fun list(user: User): ListAccountsResponse{
        return try{
            ListAccountsResponse(accounts = repository.list(user.uuid!!))
        }catch(e: Exception){
            logger.error("Error during list accounts", e)
            ListAccountsResponse(error = LIST_ACCOUNTS_ERROR)
        }
    }

    override fun get(uuid: UUID, user: User): AccountResponse {
        return try{
            AccountResponse(account = repository.get(uuid, user.uuid!!))
        }catch(e: Exception){
            logger.error("Error during get accout", e)
            AccountResponse(error = GET_ACCOUNT_ERROR)
        }
    }

    override fun delete(uuid: UUID, user: User): AccountResponse {
        return try{
            repository.delete(uuid, user.uuid!!)
            AccountResponse()
        }catch(e: Exception){
            logger.error("Error during delete accout", e)
            AccountResponse(error = DELETE_ACCOUNT_ERROR)
        }
    }
}