package com.backend.blue_lock.core.payment.infraestructure.repository

import com.backend.blue_lock.core.payment.domain.entities.Payment

interface PaymentRepository {
    fun createPayment(payment: Payment)

    fun updatePayment(payment: Payment)
}