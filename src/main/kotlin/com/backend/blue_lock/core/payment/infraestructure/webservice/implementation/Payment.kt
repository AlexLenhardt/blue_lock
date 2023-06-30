package com.backend.blue_lock.core.payment.infraestructure.webservice.implementation

import com.backend.blue_lock.core.payment.domain.entities.Payment
import com.backend.blue_lock.core.payment.domain.entities.PaymentResponse
import com.backend.blue_lock.core.payment.domain.usecase.PaymentUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class Payment(
    val usecase: PaymentUseCase
) {
    @GetMapping
    fun postPayment(@RequestBody payment: Payment): PaymentResponse {
        return usecase.postPayment(payment)
    }
}