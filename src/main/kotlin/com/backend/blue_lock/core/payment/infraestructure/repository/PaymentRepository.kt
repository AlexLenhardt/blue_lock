package com.backend.blue_lock.core.payment.infraestructure.repository

import com.backend.blue_lock.core.payment.domain.entities.Payment
import java.util.*

interface PaymentRepository {
    fun createPayment(payment: Payment)

    fun updatePayment(payment: Payment)

    fun getPayment(uuid: UUID): Payment
}