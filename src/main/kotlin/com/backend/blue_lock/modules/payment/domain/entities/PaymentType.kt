package com.backend.blue_lock.modules.payment.domain.entities

import java.util.*

class PaymentType(
    var uuid: UUID? = null,
    var userUUID: UUID? = null,
    var label: String? = null
)