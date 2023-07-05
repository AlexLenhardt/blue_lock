package com.backend.blue_lock.core.payment.domain.entities

import com.backend.blue_lock.core.shared.error.GenericError

class PaymentListResponse(
    val payments: List<Payment>? = null,
    val page: Int? = null,
    val size: Int? = null,
    val total: Int? = null,
    val numberPages: Int? = null,
    val error: GenericError? = null
)