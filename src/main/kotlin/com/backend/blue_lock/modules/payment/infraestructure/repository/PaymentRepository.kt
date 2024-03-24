package com.backend.blue_lock.modules.payment.infraestructure.repository

import com.backend.blue_lock.modules.payment.domain.entities.Payment
import com.backend.blue_lock.modules.payment.domain.entities.PaymentType
import com.backend.blue_lock.core.shared.entities.BasicFilter
import java.util.*

interface PaymentRepository {
    fun createPaymentType(paymentType: PaymentType, userUUID: UUID): PaymentType?
    
    fun updatePaymentType(paymentType: PaymentType, userUUID: UUID): PaymentType?

    fun createPayment(payment: Payment, userUUID: UUID)

    fun updatePayment(payment: Payment)

    fun getPayment(uuid: UUID): Payment?

    fun getPaymentType(uuid: UUID, userUUID: UUID): PaymentType?

    fun listPayments(
        userUUID: UUID,
        page: Int,
        size: Int,
        sortBy: String?,
        orderBy: String?,
        basicFilter: List<BasicFilter>?
    ): List<Payment>?

    fun countPayments(basicFilter: List<BasicFilter>?, userUUID: UUID): Int

    fun listPaymentType(userUUID: UUID): List<PaymentType>

    fun deletePayment(paymentUUID: UUID)
}