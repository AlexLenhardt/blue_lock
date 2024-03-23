package com.backend.blue_lock.modules.payment.infraestructure.webservice.implementation

import com.backend.blue_lock.modules.payment.domain.entities.Payment
import com.backend.blue_lock.modules.payment.domain.entities.PaymentListResponse
import com.backend.blue_lock.modules.payment.domain.entities.PaymentResponse
import com.backend.blue_lock.modules.payment.domain.entities.PaymentType
import com.backend.blue_lock.modules.payment.domain.entities.PaymentTypeResponse
import com.backend.blue_lock.modules.payment.domain.usecase.PaymentUseCase
import com.backend.blue_lock.core.shared.entities.BasicFilter
import com.backend.blue_lock.core.user.security.SystemUser
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PathVariable
import java.util.UUID
import kotlin.collections.emptyList

@RestController
@RequestMapping("/payment")
class Payment(
    val usecase: PaymentUseCase
) {
    @PostMapping
    fun postPaymentType(@RequestBody paymentType: PaymentType): PaymentTypeResponse {
        val user = SecurityContextHolder.getContext().authentication.principal as SystemUser
        return usecase.postPaymentType(paymentType, user.getUserData())
    }
    @PostMapping
    fun postPayment(@RequestBody payment: Payment): PaymentResponse {
        val user = SecurityContextHolder.getContext().authentication.principal as SystemUser
        return usecase.postPayment(payment, user.getUserData())
    }

    @DeleteMapping("/{paymentUUID}")
    fun deletePayment(@PathVariable paymentUUID: UUID?): PaymentResponse {
        return usecase.deletePayment(paymentUUID)
    }

    @GetMapping
    fun listPayments(
        @RequestParam("page", required = false, defaultValue = "1") page: Int?,
        @RequestParam("size", required = false, defaultValue = "30") size: Int?,
        @RequestParam("sortBy", required = false, defaultValue = "asc") sortBy: String?,
        @RequestParam("orderBy", required = false, defaultValue = "uuid") orderBy: String?,
        @RequestParam("filter", required = false) filter: List<BasicFilter>?
    ): PaymentListResponse {
        val user = SecurityContextHolder.getContext().authentication.principal as SystemUser
        return usecase.listPayments(
            user.getUserData(),
            page,
            size,
            sortBy,
            orderBy,
            filter?.map { BasicFilter(it.name, it.value) }
        )
    }

    @GetMapping("/{paymentUUID}")
    fun getPayment(@PathVariable paymentUUID: UUID?): PaymentResponse {
        return usecase.getPayment(paymentUUID)
    }

    @GetMapping("/type")
    fun listPaymentType(): List<PaymentType>{
        return usecase.listPaymentType()
    }
}