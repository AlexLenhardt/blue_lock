package com.backend.blue_lock.core.payment.domain.usecase.implementation

import com.backend.blue_lock.core.payment.domain.entities.Payment
import com.backend.blue_lock.core.payment.domain.entities.PaymentResponse
import com.backend.blue_lock.core.payment.domain.exception.*
import com.backend.blue_lock.core.payment.domain.usecase.PaymentUseCase
import com.backend.blue_lock.core.payment.infraestructure.repository.PaymentRepository
import org.springframework.stereotype.Service
import com.github.f4b6a3.uuid.UuidCreator
import java.util.UUID

@Service
class PaymentUseCaseImplementation(
    val repository: PaymentRepository
) : PaymentUseCase {
    override fun postPayment(payment: Payment): PaymentResponse {
        return try {
            if (payment.uuid != null) {
                repository.updatePayment(payment)
            } else {
                payment.uuid = UuidCreator.getTimeOrdered()
                repository.createPayment(payment)
            }

            PaymentResponse(payment = payment)
        } catch (e: Exception) {
            PaymentResponse(error = POST_PAYMENT_ERROR)
        }
    }

    override fun getPayment(paymentUUID: UUID?): PaymentResponse {
        if (paymentUUID == null) {
            return PaymentResponse(error = PAYMENT_NOT_INFORMED)
        }

        return try{
            val payment = repository.getPayment(paymentUUID)
            PaymentResponse(payment = payment)
        }catch(e: Exception){
            PaymentResponse(error = PAYMENT_DATABASE_ERROR)
        }
    }
}