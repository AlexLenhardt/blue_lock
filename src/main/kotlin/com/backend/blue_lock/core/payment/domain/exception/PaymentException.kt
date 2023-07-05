package com.backend.blue_lock.core.payment.domain.exception

import com.backend.blue_lock.core.shared.error.GenericError

val POST_PAYMENT_ERROR = PaymentException("POST_PAYMENT_ERROR", "Erro ao cadastrar pagamento")

val PAYMENT_DATABASE_ERROR = PaymentException("PAYMENT_DATABASE_ERROR", "Erro no banco de dados")

class PaymentException(
    code: String,
    description: String
) : GenericError("payment-module", code, description)