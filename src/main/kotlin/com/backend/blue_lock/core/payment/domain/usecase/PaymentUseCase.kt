package com.backend.blue_lock.core.payment.domain.usecase

import com.backend.blue_lock.core.payment.domain.entities.Payment
import com.backend.blue_lock.core.payment.domain.entities.PaymentListResponse
import com.backend.blue_lock.core.payment.domain.entities.PaymentResponse
import com.backend.blue_lock.core.payment.domain.entities.PaymentType
import com.backend.blue_lock.core.shared.entities.BasicFilter
import com.backend.blue_lock.core.user.domain.entities.User
import java.util.UUID

interface PaymentUseCase {
    fun getPayment(paymentUUID: UUID?): PaymentResponse 

    fun postPayment(payment: Payment, user: User): PaymentResponse
    
    fun listPayments(
        user: User,
        page: Int?,
        size: Int?,
        sortBy: String?,
        orderBy: String?,
        basicFilter: List<BasicFilter>?
    ): PaymentListResponse

    fun listPaymentType(): List<PaymentType>

    fun deletePayment(paymentUUID: UUID?): PaymentResponse
}