package com.backend.blue_lock.core.payment.domain.entities

import com.backend.blue_lock.core.shared.error.GenericError

class PaymentResponse(
    val payment: Payment? = null,
    val error: GenericError? = null,
)