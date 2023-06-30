package com.backend.blue_lock.core.payment.domain.usecase

import com.backend.blue_lock.core.payment.domain.entities.Payment
import com.backend.blue_lock.core.payment.domain.entities.PaymentResponse

interface PaymentUseCase {
    fun postPayment(payment: Payment): PaymentResponse
}