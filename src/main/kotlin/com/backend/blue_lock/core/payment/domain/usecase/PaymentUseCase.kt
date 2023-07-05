package com.backend.blue_lock.core.payment.domain.usecase

import com.backend.blue_lock.core.payment.domain.entities.Payment
import com.backend.blue_lock.core.payment.domain.entities.PaymentResponse
import java.util.UUID

interface PaymentUseCase {
    fun postPayment(payment: Payment): PaymentResponse

    fun getPayment(paymentUUID: UUID?): PaymentResponse 
}