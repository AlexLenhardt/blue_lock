package com.backend.blue_lock.core.payment.infraestructure.repository

import com.backend.blue_lock.core.payment.domain.entities.Payment
import com.backend.blue_lock.core.payment.domain.entities.PaymentType
import com.backend.blue_lock.core.shared.entities.BasicFilter
import java.util.*

interface PaymentRepository {
    fun createPayment(payment: Payment)

    fun updatePayment(payment: Payment)

    fun getPayment(uuid: UUID): Payment?

    fun getPaymentTypeByCode(code: Int): PaymentType?

    fun listPayments(
        page: Int,
        size: Int,
        sortBy: String?,
        orderBy: String?,
        basicFilter: List<BasicFilter>?
    ): List<Payment>?

    fun countPayments(basicFilter: List<BasicFilter>?): Int

    fun listPaymentType(): List<PaymentType>
}