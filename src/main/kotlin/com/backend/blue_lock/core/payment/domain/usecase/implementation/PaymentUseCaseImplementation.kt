package com.backend.blue_lock.core.payment.domain.usecase.implementation

import com.backend.blue_lock.core.payment.domain.entities.Payment
import com.backend.blue_lock.core.payment.domain.entities.PaymentResponse
import com.backend.blue_lock.core.payment.domain.exception.POST_PAYMENT_ERROR
import com.backend.blue_lock.core.payment.domain.usecase.PaymentUseCase
import com.backend.blue_lock.core.payment.infraestructure.repository.PaymentRepository
import org.springframework.stereotype.Service

@Service
class PaymentUseCaseImplementation(
    val repository: PaymentRepository
) : PaymentUseCase {
    override fun postPayment(payment: Payment): PaymentResponse {
        return try {
            if (payment.uuid != null) {
                repository.updatePayment(payment)
            } else {
                repository.createPayment(payment)
            }

            PaymentResponse(payment = payment)
        } catch (e: Exception) {
            PaymentResponse(error = POST_PAYMENT_ERROR)
        }
    }
}