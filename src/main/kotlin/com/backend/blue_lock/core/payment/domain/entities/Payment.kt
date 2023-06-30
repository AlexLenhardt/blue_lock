package com.backend.blue_lock.core.payment.domain.entities

import java.util.*

class Payment(
    var uuid: UUID? = null,
    var date: Date? = null,
    var value: Double? = null,
    var description: String? = null,
    var type: PaymentType? = null
)