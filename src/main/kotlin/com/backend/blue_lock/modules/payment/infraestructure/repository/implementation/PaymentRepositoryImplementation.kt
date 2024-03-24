package com.backend.blue_lock.modules.payment.infraestructure.repository.implementation

import com.backend.blue_lock.modules.payment.domain.entities.Payment
import com.backend.blue_lock.modules.payment.domain.entities.PaymentType
import com.backend.blue_lock.modules.payment.infraestructure.repository.PaymentRepository
import com.backend.blue_lock.modules.payment.infraestructure.repository.database.PaymentDatabase
import com.backend.blue_lock.modules.payment.infraestructure.repository.database.PaymentTypeDatabase
import com.backend.blue_lock.modules.payment.infraestructure.repository.database.withPaymentFilters
import com.backend.blue_lock.core.shared.entities.BasicFilter
import com.backend.blue_lock.core.shared.entities.EnumStatus
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.neq
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
class PaymentRepositoryImplementation : PaymentRepository {
    override fun createPaymentType(paymentType: PaymentType, userUUID: UUID): PaymentType?{
        transaction{
            PaymentTypeDatabase.insert{
                it[this.uuid] = paymentType.uuid!!
                it[this.userUUID] = userUUID
                it[this.label] = paymentType.label!!
            }
        }

        return paymentType
    }

    override fun updatePaymentType(paymentType: PaymentType, userUUID: UUID): PaymentType{
        transaction{ 
            PaymentTypeDatabase.update({
                (PaymentTypeDatabase.uuid eq paymentType.uuid!!) and
                (PaymentTypeDatabase.userUUID eq userUUID)
            }) { 
                it[this.label] = paymentType.label!!
             }
         }

         return paymentType
    }

    override fun createPayment(payment: Payment, userUUID: UUID) {
        transaction {
            PaymentDatabase.insert {
                it[this.uuid] = payment.uuid!!
                it[this.userUUID] = userUUID
                it[this.paymentType] = payment.type!!.uuid!!
                it[this.date] = payment.date!!
                it[this.value] = payment.value!!
                it[this.description] = payment.description
            }
        }
    }

    override fun updatePayment(payment: Payment) {
        transaction {
            PaymentDatabase.update({
                PaymentDatabase.uuid eq payment.uuid!!
            }) {
                it[this.paymentType] = payment.type!!.uuid!!
                it[this.date] = payment.date!!
                it[this.value] = payment.value!!
                it[this.description] = payment.description
                it[this.modifiedAt] = CurrentDateTime
            }
        }
    }

    override fun getPayment(uuid: UUID): Payment? {
        return transaction {
            PaymentDatabase
                .innerJoin(PaymentTypeDatabase, { PaymentTypeDatabase.uuid }, { PaymentDatabase.paymentType })
                .select(
                    PaymentDatabase.uuid eq uuid
                )
                .firstOrNull()
                ?.toPayment()
        }
    }

    override fun getPaymentType(uuid: UUID, userUUID: UUID): PaymentType? {
        return transaction {
            PaymentTypeDatabase
                .select(
                    (PaymentTypeDatabase.uuid eq uuid) and 
                    (PaymentTypeDatabase.userUUID eq userUUID)
                )
                .firstOrNull()?.toPaymentType()
        }
    }

    override fun listPayments(
        userUUID: UUID,
        page: Int,
        size: Int,
        sortBy: String?,
        orderBy: String?,
        basicFilter: List<BasicFilter>?
    ): List<Payment>? {
        return transaction {
            PaymentDatabase
                .innerJoin(PaymentTypeDatabase, { paymentType }, { uuid })
                .slice(
                    PaymentDatabase.uuid,
                    PaymentDatabase.description,
                    PaymentDatabase.date,
                    PaymentDatabase.value,
                    PaymentTypeDatabase.label,
                    PaymentTypeDatabase.uuid,
                )
                .select(
                    (PaymentDatabase.userUUID eq userUUID) and
                            (PaymentDatabase.statusCode neq EnumStatus.Deleted.value)
                )
                .limit(size, offset = (((page - 1) * size).toLong()))
                .orderBy(
                    when (sortBy) {
                        "desc" -> when (orderBy) {
                            "description" -> PaymentDatabase.description to SortOrder.DESC
                            "value" -> PaymentDatabase.value to SortOrder.DESC
                            "date" -> PaymentDatabase.date to SortOrder.DESC
                            "type" -> PaymentDatabase.paymentType to SortOrder.DESC

                            else -> PaymentDatabase.modifiedAt to SortOrder.DESC
                        }

                        "asc" -> when (orderBy) {
                            "description" -> PaymentDatabase.description to SortOrder.ASC
                            "value" -> PaymentDatabase.value to SortOrder.ASC
                            "date" -> PaymentDatabase.date to SortOrder.ASC
                            "type" -> PaymentDatabase.paymentType to SortOrder.ASC

                            else -> PaymentDatabase.modifiedAt to SortOrder.ASC
                        }

                        else -> error("VALUE NOT FOUND")
                    }
                )
                .withPaymentFilters(basicFilter)
                .map {
                    it.toPayment()
                }
        }
    }

    override fun countPayments(basicFilter: List<BasicFilter>?, userUUID: UUID): Int {
        return transaction {
            PaymentDatabase
                .innerJoin(PaymentTypeDatabase, { paymentType }, { uuid })
                .select(
                    (PaymentDatabase.userUUID eq userUUID) and 
                    (PaymentDatabase.statusCode neq EnumStatus.Deleted.value )
                )
                .withPaymentFilters(basicFilter)
                .count()
                .toInt()
        }
    }

    override fun listPaymentType(userUUID: UUID): List<PaymentType> {
        return transaction {
            PaymentTypeDatabase
                .select(
                    PaymentTypeDatabase.userUUID eq userUUID
                )
                .map {
                    it.toPaymentType()
                }
        }
    }

    override fun deletePayment(paymentUUID: UUID) {
        transaction {
            PaymentDatabase.update({
                PaymentDatabase.uuid eq paymentUUID
            }) {
                it[this.statusCode] = EnumStatus.Deleted.value
            }
        }
    }
}

private fun ResultRow.toPayment(): Payment {
    return Payment(
        uuid = this.getOrNull(PaymentDatabase.uuid),
        description = this.getOrNull(PaymentDatabase.description),
        type = this.toPaymentType(),
        value = this.getOrNull(PaymentDatabase.value),
        date = this.getOrNull(PaymentDatabase.date),
    )
}

private fun ResultRow.toPaymentType(): PaymentType {
    return PaymentType(
        uuid = this.getOrNull(PaymentTypeDatabase.uuid),
        userUUID = this.getOrNull(PaymentTypeDatabase.userUUID),
        label = this.getOrNull(PaymentTypeDatabase.label)
    )
}

