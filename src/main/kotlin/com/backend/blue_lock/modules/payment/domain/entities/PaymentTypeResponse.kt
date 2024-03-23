package com.backend.blue_lock.modules.payment.domain.entities

import com.backend.blue_lock.core.shared.error.GenericError

class PaymentTypeResponse(
    val paymentType: PaymentType? = null,
    val error: GenericError? = null,
)