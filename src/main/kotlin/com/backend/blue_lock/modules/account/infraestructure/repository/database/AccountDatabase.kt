package com.backend.blue_lock.modules.account.infraestructure.repository.database

import org.jetbrains.exposed.sql.*
import java.util.UUID
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import com.backend.blue_lock.core.user.infraestructure.repository.database.UserTable

object AccountDatabase: Table("account"){
    val uuid = uuid("uuid").uniqueIndex()
    val label = varchar("label", 60).uniqueIndex()
    val userUUID = uuid("user_uuid").references(UserTable.uuid)
    val statusCode = integer("status_code").default(0)
    val modifiedAt = datetime("modified_at").defaultExpression(CurrentDateTime)
    val createAt = datetime("created_at").defaultExpression(CurrentDateTime)

    init {
        PrimaryKey(uuid)
    }
}