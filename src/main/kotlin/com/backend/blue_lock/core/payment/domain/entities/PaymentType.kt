package com.backend.blue_lock.core.payment.domain.entities

import java.util.*

class PaymentType(
    var uuid: UUID? = null,
    var code: Int? = null,
    var label: String? = null
)