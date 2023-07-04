package com.backend.blue_lock.core.payment.infraestructure.repository.database

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object PaymentTypeDatabase: Table("payment_type") {
    val uuid = uuid("uuid").uniqueIndex()
    val code = integer("code").uniqueIndex()
    val label = varchar("label", 100)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)

    init {
        PrimaryKey(uuid)
    }
}