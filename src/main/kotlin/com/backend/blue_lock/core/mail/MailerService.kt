package com.backend.blue_lock.core.mail

import org.springframework.stereotype.Component

/**
 * Simple service to send internal e-mail messages
 */
@Component
interface MailerService {
    /**
     * Sends a simple e-mail message to one destination
     */
    fun simple(to: String, cc: String, subject: String, message: String): Boolean
}