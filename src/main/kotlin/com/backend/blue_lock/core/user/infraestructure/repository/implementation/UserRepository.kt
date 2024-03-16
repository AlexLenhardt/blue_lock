package com.backend.blue_lock.core.user.infraestructure.repository.implementation

import com.backend.blue_lock.core.user.domain.entities.User
import com.backend.blue_lock.core.user.domain.entities.UserType
import com.backend.blue_lock.core.user.domain.repository.UserRepository
import com.backend.blue_lock.core.user.domain.usecases.response.UserFilter
import com.backend.blue_lock.core.user.infraestructure.repository.database.UserTable
import com.backend.blue_lock.core.user.infraestructure.repository.database.UserTypeTable
import com.backend.blue_lock.core.user.infraestructure.repository.database.withUserFilters
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class UserRepository : UserRepository {
    override fun getUserByAuthenticationRecord(authenticationRecord: String): User? = transaction {
        (UserTable innerJoin UserTypeTable)
            .select { UserTable.authenticationRecord eq authenticationRecord }
            .firstOrNull()?.toUser()
    }

    override fun getUserByUUID(uuid: UUID): User? = transaction {
        (UserTable innerJoin UserTypeTable).select { UserTable.uuid eq uuid }.firstOrNull()?.toUser()
    }

    override fun getUserByEmail(email: String): User? = transaction {
        (UserTable innerJoin UserTypeTable).select { UserTable.email eq email }.firstOrNull()?.toUser()
    }

    override fun getUserType(userType: UserType): UserType? {
        if (userType.uuid != null){
            return try {
                transaction{
                    UserTypeTable.select{
                        (UserTypeTable.uuid eq userType.uuid)
                    }.firstOrNull()
                    ?.toUserType()
                }
            } catch(e: Exception) {
                null
            }
        }else if(userType.code != null){
            return try {
                transaction{
                    UserTypeTable.select{
                        (UserTypeTable.code eq userType.code)
                    }.firstOrNull()
                    ?.toUserType()
                }
            } catch(e: Exception) {
                null
            }
        }
        

        return null
    }

    override fun listUserType(): List<UserType> = transaction {
        UserTypeTable.selectAll().map { it.toUserType() }
    }

    override fun updateUser(user: User): User = transaction {
        UserTable.update({
            UserTable.uuid eq user.uuid!!
        }) {
            it[authenticationRecord] = user.authenticationRecord!!
            it[name] = user.name!!
            it[email] = user.email
            it[userType] = user.userType!!.uuid!!
            it[contact] = user.contact
            it[isActive] = user.isActive
        }

        user
    }

    override fun listAllUsers(
        firstItem: Int,
        size: Int,
        orderBy: String,
        sortBy: String,
        filter: List<UserFilter>?,
    ): List<User> {
        return transaction {
            (UserTable innerJoin UserTypeTable)
                .selectAll()
                .withUserFilters(filter)
                .limit(size, offset = firstItem.toLong())
                .orderBy(
                    when (sortBy) {
                        "asc" -> when (orderBy) {
                            "name" -> UserTable.name to SortOrder.ASC
                            "userType" -> UserTable.userType to SortOrder.ASC
                            "contact" -> UserTable.contact to SortOrder.ASC
                            "email" -> UserTable.email to SortOrder.ASC
                            "authenticationRecord" -> UserTable.authenticationRecord to SortOrder.ASC
                            else -> UserTable.modifiedAt to SortOrder.ASC
                        }

                        "desc" -> when (orderBy) {
                            "name" -> UserTable.name to SortOrder.DESC
                            "userType" -> UserTable.userType to SortOrder.DESC
                            "contact" -> UserTable.contact to SortOrder.DESC
                            "email" -> UserTable.email to SortOrder.DESC
                            "authenticationRecord" -> UserTable.authenticationRecord to SortOrder.DESC
                            else -> UserTable.modifiedAt to SortOrder.DESC
                        }

                        else -> error("IMPOSSIBLE ORDER")
                    }
                )
                .map { it.toUser() }
                .toList()
        }
    }

    override fun getCountUsers(filter: List<UserFilter>?): Int = transaction {
        UserTable.selectAll().withUserFilters(filter).count().toInt()
    }
}

private fun ResultRow.toUser(): User {
    return User(
        uuid = this[UserTable.uuid],
        name = this[UserTable.name],
        userType = UserType(
            this[UserTable.userType],
            this.getOrNull(UserTypeTable.code) ?: 0,
            this.getOrNull(UserTypeTable.label) ?: ""
        ),
        authenticationRecord = this[UserTable.authenticationRecord],
        contact = this[UserTable.contact],
        email = this[UserTable.email],
        isActive = this[UserTable.isActive],
    )
}

private fun ResultRow.toUserType(): UserType {
    return UserType(
        uuid = this[UserTypeTable.uuid],
        code = this[UserTypeTable.code],
        label = this[UserTypeTable.label],
    )
}
