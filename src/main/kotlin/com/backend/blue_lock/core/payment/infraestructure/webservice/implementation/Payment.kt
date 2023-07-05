package com.backend.blue_lock.core.payment.infraestructure.webservice.implementation

import com.backend.blue_lock.core.payment.domain.entities.Payment
import com.backend.blue_lock.core.payment.domain.entities.PaymentResponse
import com.backend.blue_lock.core.payment.domain.usecase.PaymentUseCase
import com.backend.blue_lock.core.payment.infraestructure.repository.PaymentRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.PathVariable
import java.util.UUID

@RestController
@RequestMapping("/payment")
class Payment(
    val usecase: PaymentUseCase
) {
    @PostMapping
    fun postPayment(@RequestBody payment: Payment): PaymentResponse {
        return usecase.postPayment(payment)
    }

    @GetMapping("/{paymentUUID}")
    fun getPayment(@PathVariable paymentUUID: UUID?): PaymentResponse {
        println(paymentUUID)
        return usecase.getPayment(paymentUUID)
    }
}