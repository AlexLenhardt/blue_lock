package com.backend.blue_lock.core.payment.domain.exception

import com.backend.blue_lock.core.shared.error.GenericError

val POST_PAYMENT_ERROR = PaymentException("POST_PAYMENT_ERROR", "Erro ao cadastrar pagamento")

class PaymentException(
    code: String,
    description: String
) : GenericError("payment-module", code, description)