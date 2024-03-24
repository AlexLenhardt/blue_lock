package com.backend.blue_lock.modules.payment.domain.exception

import com.backend.blue_lock.core.shared.error.GenericError

val PAYMENT_TYPE_LABEL_NOT_INFORMED = PaymentException("PAYMENT_TYPE_LABEL_NOT_INFORMED", "Descrição do tipo de pagamento não informado")

val POST_PAYMENT_ERROR = PaymentException("POST_PAYMENT_ERROR", "Erro ao cadastrar pagamento")

val PAYMENT_NOT_INFORMED = PaymentException("PAYMENT_NOT_INFORMED", "Pagamento não informado")

val PAYMENT_DATABASE_ERROR = PaymentException("PAYMENT_DATABASE_ERROR", "Erro no banco de dados")

val PAYMENT_TYPE_NOT_INFORMED = PaymentException("PAYMENT_TYPE_NOT_INFORMED", "Tipo de pagamento não informado")

val POST_PAYMENT_TYPE_ERROR = PaymentException("POST_PAYMENT_TYPE_ERROR", "Houve um erro ao criar/atualizar o tipo de pagament")

class PaymentException(
    code: String,
    description: String
) : GenericError("payment-module", code, description)