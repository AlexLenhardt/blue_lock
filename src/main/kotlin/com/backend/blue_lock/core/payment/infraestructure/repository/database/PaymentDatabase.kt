package com.backend.blue_lock.core.payment.infraestructure.repository.database

import com.backend.blue_lock.core.shared.utils.Utils
import com.backend.blue_lock.core.shared.entities.BasicFilter
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime
import java.util.*

object PaymentDatabase : Table("payments") {
    val uuid = uuid("uuid").uniqueIndex()
    val date = date("date")
    val value = double("value")
    val paymentType = uuid("payment_type").references(PaymentTypeDatabase.uuid)
    val description = text("description").nullable()
    val statusCode = integer("status_code")
    val modifiedAt = datetime("modified_at").defaultExpression(CurrentDateTime)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)

    init {
        PrimaryKey(uuid)
    }
}

fun Query.withPaymentFilters(filter: List<BasicFilter>?): Query {
    if (filter == null) {
        return this
    }
    val filters = filter.filter { it.name.isNotBlank() }.map {
        when (it.name) {
            "date" -> Op.build {
                PaymentDatabase.date greaterEq Utils.dateConverter(it.value) and (PaymentDatabase.date lessEq Utils.dateConverter(
                    it.value
                ))
            }

            "type" -> Op.build { PaymentDatabase.paymentType eq UUID.fromString(it.value) }

            else -> throw Exception("invalid column name: ${it.name}")
        }
    }

    filters.forEach { this.andWhere { it } }

    return this
}