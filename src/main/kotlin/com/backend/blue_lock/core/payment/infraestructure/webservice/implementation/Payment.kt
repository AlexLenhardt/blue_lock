package com.backend.blue_lock.core.payment.infraestructure.webservice.implementation

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class Payment {
    @GetMapping
    fun teste(): String {
        println("teste")
        return "Teste"
    }
}