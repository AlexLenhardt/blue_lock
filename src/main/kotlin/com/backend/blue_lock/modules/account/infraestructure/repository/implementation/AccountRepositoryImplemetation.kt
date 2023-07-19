package com.backend.blue_lock.modules.account.infraestructure.repository.implementation

import com.backend.blue_lock.modules.account.infraestructure.repository.AccountRepository
import com.backend.blue_lock.modules.account.infraestructure.repository.database.AccountDatabase
import com.backend.blue_lock.modules.account.domain.entities.Account
import com.backend.blue_lock.core.shared.entities.EnumStatus
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.neq
import org.jetbrains.exposed.sql.*
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class AccountRepositoryImplementation: AccountRepository{
    override fun update(account: Account) {
        transaction {
            AccountDatabase.update({
                AccountDatabase.uuid eq account.uuid!!
            }) {
                it[this.label] = account.label!!
            }
        }
    }

    override fun create(account: Account) {
        transaction {
            AccountDatabase.insert{
                it[AccountDatabase.uuid] = account.uuid!!
                it[AccountDatabase.label] = account.label!!
            }
        }
    }

    override fun list(): List<Account> {
        return transaction {
            AccountDatabase
            .select { AccountDatabase.statusCode neq EnumStatus.Deleted.value }
            .map {
                Account(
                    uuid = it[AccountDatabase.uuid],
                    label = it[AccountDatabase.label]
                )
            }
        }
    }

    override fun get(uuid: UUID): Account? {
        return transaction {
            AccountDatabase
                .select{ AccountDatabase.uuid eq uuid }
                .firstOrNull()
                ?.let{
                    Account(
                        uuid = it[AccountDatabase.uuid],
                        label = it[AccountDatabase.label]
                    )
                }
        }
    }
}