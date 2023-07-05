package com.backend.blue_lock.core.payment.domain.usecase

import com.backend.blue_lock.core.payment.domain.entities.Payment
import com.backend.blue_lock.core.payment.domain.entities.PaymentListResponse
import com.backend.blue_lock.core.payment.domain.entities.PaymentResponse
import com.backend.blue_lock.core.shared.entities.BasicFilter
import java.util.UUID

interface PaymentUseCase {
    fun getPayment(paymentUUID: UUID?): PaymentResponse 

    fun postPayment(payment: Payment): PaymentResponse
    
    fun listPayments(
        page: Int?,
        size: Int?,
        sortBy: String?,
        orderBy: String?,
        basicFilter: List<BasicFilter>?
    ): PaymentListResponse
}