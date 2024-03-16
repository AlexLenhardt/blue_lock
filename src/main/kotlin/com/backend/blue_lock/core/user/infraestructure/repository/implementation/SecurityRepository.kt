package com.backend.blue_lock.core.user.infraestructure.repository.implementation

import com.backend.blue_lock.core.user.domain.entities.User
import com.backend.blue_lock.core.user.domain.entities.UserType
import com.backend.blue_lock.core.user.domain.repository.SecurityRepository
import com.backend.blue_lock.core.user.infraestructure.repository.database.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class SecurityRepository : SecurityRepository {
    override fun findUserByUUID(uuid: UUID): User? {
        return transaction {
            (UserTable innerJoin UserTypeTable)
                .select { UserTable.uuid eq uuid }
                .limit(1)
                .map { it.toUser() }
                .firstOrNull()
        }
    }

    override fun findUserByEmail(email: String): User? {
        return transaction {
            (UserTable innerJoin UserTypeTable)
                .select { UserTable.email eq email }
                .limit(1)
                .map { it.toUser() }
                .firstOrNull()
        }
    }

    override fun findUserByAuthenticationRecord(authenticationRecord: String): User? {
        return transaction {
            (UserTable innerJoin UserTypeTable)
                .select { UserTable.authenticationRecord eq authenticationRecord }
                .limit(1)
                .map { it.toUser() }
                .firstOrNull()
        }
    }

    override fun listUserRoles(uuid: UUID): List<String> {
        return transaction {
            PermissionTable
                .innerJoin(ModuleRoleTable, { ModuleRoleTable.uuid }, { PermissionTable.moduleRoleUUID })
                .select {
                    PermissionTable.userUUID eq uuid
                }
                .map {
                    it[ModuleRoleTable.role]
                }
        }
    }

    override fun listOwnerRoles(): List<String> {
        return transaction {
            ModuleRoleTable
                .selectAll()
                .map {
                    it[ModuleRoleTable.role]
                }
        }
    }

    override fun createUser(user: User) {
        transaction {
            UserTable.insert {
                it[uuid] = user.uuid!!
                it[name] = user.name!!
                it[authenticationRecord] = user.authenticationRecord!!
                it[contact] = user.contact.toString()
                it[email] = user.email
                it[isActive] = user.isActive
                it[password] = user.passwordHash!!
                it[userType] = user.userType!!.uuid!!
                it[updatePassword] = true
            }
        }
    }

    override fun createPasswordResetRequest(userUUID: UUID, requestUUID: UUID, expiresAt: LocalDateTime) {
        transaction {
            // delete any old request, the user can have only one pending request at a time
            PasswordResetRequestTable.deleteWhere { uuidUser eq userUUID }

            // insert the new password reset request
            PasswordResetRequestTable.insert {
                it[uuid] = requestUUID
                it[uuidUser] = userUUID
                it[PasswordResetRequestTable.expiresAt] = expiresAt
            }
        }
    }

    override fun updateUserPassword(uuid: UUID, newPasswordHash: String, forcePasswordChange: Boolean) {
        transaction {
            UserTable.update({ UserTable.uuid eq uuid }) {
                it[password] = newPasswordHash
                it[lastPasswordModified] = LocalDateTime.now()
                it[updatePassword] = forcePasswordChange
            }
        }
    }

    override fun findUserFromPasswordResetRequest(requestUUID: UUID): UUID? = transaction {
        PasswordResetRequestTable
            .slice(PasswordResetRequestTable.uuidUser)
            .select {
                (PasswordResetRequestTable.uuid eq requestUUID) and
                        (PasswordResetRequestTable.expiresAt greater LocalDateTime.now())
            }
            .firstOrNull()
            ?.let { it[PasswordResetRequestTable.uuidUser] }
    }

    override fun deletePasswordResetRequest(requestUUID: UUID) {
        transaction {
            PasswordResetRequestTable.deleteWhere { uuid eq requestUUID }
        }
    }
}

private fun ResultRow.toUser(): User {
    return User(
        this[UserTable.uuid],
        this[UserTable.name],
        UserType(
            this[UserTable.userType],
            this.getOrNull(UserTypeTable.code) ?: 0,
            this.getOrNull(UserTypeTable.label) ?: "",
        ),
        this[UserTable.isActive],
        this[UserTable.email],
        this[UserTable.authenticationRecord],
        null,
        this[UserTable.password],
        this[UserTable.contact],
        this[UserTable.updatePassword]
    )
}