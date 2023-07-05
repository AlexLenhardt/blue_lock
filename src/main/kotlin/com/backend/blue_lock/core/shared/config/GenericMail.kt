package com.backend.blue_lock.core.shared.config

import jakarta.mail.internet.MimeMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper

abstract class GenericMail(
    var from: String,
    var to: String,
    var subject: String,
    var body: String
){
    fun send(emailSender: JavaMailSender) : Boolean {
        return try {
            val message: MimeMessage = emailSender.createMimeMessage()
            message.subject = subject
            val helper = MimeMessageHelper(message, true)
            helper.setFrom(from)
            helper.setTo(to)
            helper.setText(body, true)
            emailSender.send(message)
            true
        } catch (exception: Exception){
            false
        }
    }
}