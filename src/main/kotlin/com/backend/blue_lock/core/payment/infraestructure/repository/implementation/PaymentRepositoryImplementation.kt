package com.backend.blue_lock.core.payment.infraestructure.repository.implementation

import com.backend.blue_lock.core.payment.domain.entities.Payment
import com.backend.blue_lock.core.payment.infraestructure.repository.PaymentRepository
import org.springframework.stereotype.Repository

@Repository
class PaymentRepositoryImplementation: PaymentRepository {
    override fun createPayment(payment: Payment) {
        TODO("Not yet implemented")
    }

    override fun updatePayment(payment: Payment) {
        TODO("Not yet implemented")
    }
}