package com.backend.blue_lock.core.payment.domain.exception

import com.backend.blue_lock.core.shared.error.GenericError

val POST_PAYMENT_ERROR = PaymentException("POST_PAYMENT_ERROR", "Erro ao cadastrar pagamento")

val PAYMENT_NOT_INFORMED = PaymentException("PAYMENT_NOT_INFORMED", "Pagamento não informado")

val PAYMENT_DATABASE_ERROR = PaymentException("PAYMENT_DATABASE_ERROR", "Erro no banco de dados")

val PAYMENT_TYPE_NOT_INFORMED = PaymentException("PAYMENT_TYPE_NOT_INFORMED", "Tipo de pagamento não informado")

class PaymentException(
    code: String,
    description: String
) : GenericError("payment-module", code, description)