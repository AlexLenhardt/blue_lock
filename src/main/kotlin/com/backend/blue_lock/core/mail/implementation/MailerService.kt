package com.backend.blue_lock.core.mail.implementation

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.MailException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import com.backend.blue_lock.core.mail.MailerService

@Service
class MailerService(
    private val mailSender: JavaMailSender,
) : MailerService {
    companion object {
        private val logger = LoggerFactory.getLogger(MailerService::class.java)
    }

    @Value("\${spring.mail.email}")
    private lateinit var basicFrom: String

    override fun simple(to: String, cc: String, subject: String, message: String): Boolean {
        return try {
            val mimeMessage = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(mimeMessage, true)

            helper.setFrom(basicFrom)
            helper.setSubject(subject)
            helper.setTo(to)
            if (cc.trim() != "") helper.setCc(cc)
            helper.setText(message, true)

            mailSender.send(helper.mimeMessage)

            true
        } catch (e: MailException) {
            logger.error("Could not send simple mail: ${e.message}")
            return false
        }
    }
}
