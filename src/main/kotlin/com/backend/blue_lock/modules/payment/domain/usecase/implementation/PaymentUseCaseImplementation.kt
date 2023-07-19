package com.backend.blue_lock.modules.payment.domain.usecase.implementation

import com.backend.blue_lock.modules.payment.domain.entities.Payment
import com.backend.blue_lock.modules.payment.domain.entities.PaymentListResponse
import com.backend.blue_lock.modules.payment.domain.entities.PaymentResponse
import com.backend.blue_lock.modules.payment.domain.entities.PaymentType
import com.backend.blue_lock.modules.payment.domain.exception.*
import com.backend.blue_lock.modules.payment.domain.exception.PAYMENT_DATABASE_ERROR
import com.backend.blue_lock.modules.payment.domain.exception.POST_PAYMENT_ERROR
import com.backend.blue_lock.modules.payment.domain.usecase.PaymentUseCase
import com.backend.blue_lock.modules.payment.infraestructure.repository.PaymentRepository
import com.backend.blue_lock.core.shared.entities.BasicFilter
import com.backend.blue_lock.core.user.domain.entities.User
import org.springframework.stereotype.Service
import com.github.f4b6a3.uuid.UuidCreator
import org.slf4j.LoggerFactory
import java.util.UUID
import kotlin.math.ceil

@Service
class PaymentUseCaseImplementation(
    val repository: PaymentRepository
) : PaymentUseCase {
    companion object {
        private val logger = LoggerFactory.getLogger(PaymentUseCaseImplementation::class.java)
    }

    override fun postPayment(payment: Payment, user: User): PaymentResponse {
        return try {
            if (payment.type != null && payment.type?.code != 0) {
                payment.type = repository.getPaymentTypeByCode(payment.type!!.code!!)
                    ?: return PaymentResponse(error = PAYMENT_TYPE_NOT_INFORMED)
            } else {
                return PaymentResponse(error = PAYMENT_TYPE_NOT_INFORMED)
            }

            if (payment.uuid != null) {
                repository.updatePayment(payment)
            } else {
                payment.uuid = UuidCreator.getTimeOrdered()
                repository.createPayment(payment, user.uuid!!)
            }

            PaymentResponse(payment = payment)
        } catch (e: Exception) {
            logger.error("POST_PAYMENT_ERROR", e)
            PaymentResponse(error = POST_PAYMENT_ERROR)
        }
    }

    override fun getPayment(paymentUUID: UUID?): PaymentResponse {
        if (paymentUUID == null) {
            return PaymentResponse(error = PAYMENT_NOT_INFORMED)
        }

        return try {
            val payment = repository.getPayment(paymentUUID)
            PaymentResponse(payment = payment)
        } catch (e: Exception) {
            PaymentResponse(error = PAYMENT_DATABASE_ERROR)
        }
    }

    override fun listPayments(
        user: User,
        page: Int?,
        size: Int?,
        sortBy: String?,
        orderBy: String?,
        basicFilter: List<BasicFilter>?
    ): PaymentListResponse {
        var sizeList = size
        var actualPage = page
        val numberOfRegister: Int = repository.countPayments(basicFilter, user.uuid!!)
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
                    user.uuid,
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

    override fun listPaymentType(): List<PaymentType> {
        return repository.listPaymentType()
    }

    override fun deletePayment(paymentUUID: UUID?): PaymentResponse {
        if (paymentUUID == null) {
            return PaymentResponse(error = PAYMENT_NOT_INFORMED)
        }

        try {
            repository.deletePayment(paymentUUID)
        } catch (e: Exception) {
            logger.error("PAYMENT_DELETE_ERROR", e)
            return PaymentResponse(error = PAYMENT_DATABASE_ERROR)
        }

        return PaymentResponse()
    }
}