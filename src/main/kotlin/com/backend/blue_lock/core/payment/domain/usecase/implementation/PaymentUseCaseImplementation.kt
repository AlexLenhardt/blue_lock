package com.backend.blue_lock.core.payment.domain.usecase.implementation

import com.backend.blue_lock.core.payment.domain.entities.Payment
import com.backend.blue_lock.core.payment.domain.entities.PaymentListResponse
import com.backend.blue_lock.core.payment.domain.entities.PaymentResponse
import com.backend.blue_lock.core.payment.domain.exception.*
import com.backend.blue_lock.core.payment.domain.exception.PAYMENT_DATABASE_ERROR
import com.backend.blue_lock.core.payment.domain.exception.POST_PAYMENT_ERROR
import com.backend.blue_lock.core.payment.domain.usecase.PaymentUseCase
import com.backend.blue_lock.core.payment.infraestructure.repository.PaymentRepository
import com.backend.blue_lock.core.shared.entities.BasicFilter
import org.springframework.stereotype.Service
import com.github.f4b6a3.uuid.UuidCreator
import java.util.UUID
import kotlin.math.ceil

@Service
class PaymentUseCaseImplementation(
    val repository: PaymentRepository
) : PaymentUseCase {
    override fun postPayment(payment: Payment): PaymentResponse {
        return try {
            if (payment.uuid != null) {
                repository.updatePayment(payment)
            } else {
                payment.uuid = UuidCreator.getTimeOrdered()
                repository.createPayment(payment)
            }

            PaymentResponse(payment = payment)
        } catch (e: Exception) {
            PaymentResponse(error = POST_PAYMENT_ERROR)
        }
    }

    override fun getPayment(paymentUUID: UUID?): PaymentResponse {
        if (paymentUUID == null) {
            return PaymentResponse(error = PAYMENT_NOT_INFORMED)
        }

        return try{
            val payment = repository.getPayment(paymentUUID)
            PaymentResponse(payment = payment)
        }catch(e: Exception){
            PaymentResponse(error = PAYMENT_DATABASE_ERROR)
        }
    }
    
    override fun listPayments(
        page: Int?,
        size: Int?,
        sortBy: String?,
        orderBy: String?,
        basicFilter: List<BasicFilter>?
    ): PaymentListResponse {
        var sizeList = size
        var actualPage = page
        val numberOfRegister: Int = repository.countPayments(basicFilter)
        var numberPages = 0

        if (sizeList == null) {
            sizeList = 30
        }
        if (actualPage == null) {
            actualPage = 1
        }

        if (numberOfRegister != 0) {
            numberPages = ceil(numberOfRegister.toDouble() / sizeList.toDouble()).toInt()
        }
        return try {
            PaymentListResponse(
                payments = repository.listPayments(
                    actualPage,
                    sizeList,
                    sortBy,
                    orderBy,
                    basicFilter,
                ), page = actualPage, size = sizeList, numberPages = numberPages, total = numberOfRegister
            )
        } catch (e: Exception) {
            PaymentListResponse(error = PAYMENT_DATABASE_ERROR)
        }
    }
}