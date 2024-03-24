package com.backend.blue_lock.modules.payment.infraestructure.repository.database

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import com.backend.blue_lock.core.user.infraestructure.repository.database.UserTable

object PaymentTypeDatabase: Table("payment_type") {
    val uuid = uuid("uuid").uniqueIndex()
    val userUUID = uuid("user_uuid").references(UserTable.uuid)
    val label = varchar("label", 100)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)

    init {
        PrimaryKey(uuid)
    }
}