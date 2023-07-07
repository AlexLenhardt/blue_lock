package com.backend.blue_lock.core.user.infraestructure.repository.database

import com.backend.blue_lock.core.shared.entities.EnumStatus
import org.jetbrains.exposed.sql.*
import com.backend.blue_lock.core.user.domain.usecases.response.UserFilter
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import java.lang.Exception
import java.util.*

/**
 * The exposed definition of table used to store the user
 */
object UserTable : Table("user") {
    val uuid = uuid("uuid").uniqueIndex()
    val name = varchar("name", 200)
    val userType = reference("user_type", UserTypeTable.uuid)
    val isActive = bool("is_active")
    val email = varchar("email", 200).nullable()
    val authenticationRecord = varchar("authentication_record", 200).uniqueIndex()
    val password = text("password")
    val lastPasswordModified = datetime("last_password_modified").nullable()
    val passwordValidate = datetime("password_validate").nullable()
    val updatePassword = bool("update_password").default(true)
    val contact = varchar("contact", 120).nullable()
    val modifiedAt = datetime("modified_at").defaultExpression(CurrentDateTime)
    val createAt = datetime("created_at").defaultExpression(CurrentDateTime)

    init {
        PrimaryKey(uuid)
    }
}

/**
 * The exposed definition of table used to store user type
 */
object UserTypeTable : Table("user_type") {
    val uuid = uuid("uuid")
    val label = varchar("label", 60)
    val code = integer("code").uniqueIndex()
    val statusCode = integer("status_code").default(EnumStatus.Created.value)
    val modifiedAt = datetime("modified_at").defaultExpression(CurrentDateTime)
    val createAt = datetime("created_at").defaultExpression(CurrentDateTime)

    init {
        PrimaryKey(uuid)
        uniqueIndex(uuid)
    }
}

fun Query.withUserFilters(filter: List<UserFilter>?): Query {
    if (filter == null) {
        return this
    }
    val filters = filter.filter { it.name.isNotBlank() }.map {
        when (it.name) {
            "name" -> Op.build { UserTable.name.lowerCase() like "%" + it.value.lowercase() + "%" }
            "userType" -> Op.build { UserTable.userType eq UUID.fromString(it.value) }
            "isActive" -> Op.build { UserTable.isActive eq it.value.toBoolean() }
            "email" -> Op.build { UserTable.email.lowerCase() like "%" + it.value.lowercase() + "%" }
            "authenticationRecord" -> Op.build { UserTable.authenticationRecord.lowerCase() eq it.value.lowercase() }
            "contact" -> Op.build { UserTable.contact.lowerCase() like "%" + it.value.lowercase() + "%" }
            else -> throw Exception("invalid column name: ${it.name}")
        }
    }

    filters.forEach { this.andWhere { it } }

    return this
}

/**
 * The exposed definition of the table used to manage password reset requests.
 * We store
 */
object PasswordResetRequestTable : Table("password_reset_request") {
    val uuid = uuid("uuid")
    val uuidUser = reference("user", UserTable.uuid)
    val expiresAt = datetime("expires_at")
    val createAt = datetime("created_at").defaultExpression(CurrentDateTime)

    init {
        PrimaryKey(uuid)
    }
}