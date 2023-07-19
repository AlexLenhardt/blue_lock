package com.backend.blue_lock.modules.payment.domain.entities

import java.time.LocalDate
import java.util.*
import com.backend.blue_lock.modules.payment.domain.entities.PaymentType

data class Payment(
    var uuid: UUID? = null,
    var date: LocalDate? = null,
    var value: Double? = null,
    var description: String? = null,
    var type: PaymentType? = null
)