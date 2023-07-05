package com.backend.blue_lock.core.payment.domain.exception

import com.backend.blue_lock.core.shared.error.GenericError

val POST_PAYMENT_ERROR = PaymentException("POST_PAYMENT_ERROR", "Erro ao cadastrar pagamento")

val PAYMENT_NOT_INFORMED = PaymentException("PAYMENT_NOT_INFORMED", "Pagamento não informado")

val PAYMENT_DATABASE_ERROR = PaymentException("PAYMENT_DATABASE_ERROR", "Erro no repositório")

class PaymentException(
    code: String,
    description: String
) : GenericError("payment-module", code, description)