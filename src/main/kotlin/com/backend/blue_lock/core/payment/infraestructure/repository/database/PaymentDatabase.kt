package com.backend.blue_lock.core.payment.infraestructure.repository.database

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime

object PaymentDatabase : Table("payments") {
    val uuid = uuid("uuid").uniqueIndex()
    val date = date("date")
    val value = double("value")
    val paymentType = uuid("payment_type").references(PaymentTypeDatabase.uuid)
    val description = text("description").nullable()
    val statusCode = integer("status_code").default(0)
    val modifiedAt = datetime("modified_at").defaultExpression(CurrentDateTime)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)

    init {
        PrimaryKey(uuid)
    }
}