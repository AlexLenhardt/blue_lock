package com.backend.blue_lock.core.payment.domain.entities

import java.util.*

class Payment(
    var uuid: UUID,
    var date: Date,
    var value: Double,
    var description: String,
    var type: PaymentType
)