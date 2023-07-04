package com.backend.blue_lock.core.payment.domain.entities

import java.time.LocalDate
import java.util.*

data class Payment(
    var uuid: UUID? = null,
    var date: LocalDate? = null,
    var value: Double? = null,
    var description: String? = null,
    var type: PaymentType? = null
)