package com.backend.blue_lock.core.user.domain.email

import com.backend.blue_lock.core.shared.config.GenericMail

const val DEFAULT_EMAIL_RESET_URL = "https://192.168.3.11:3000/password/reset"
class UserEmail(
    to: String,
    subject: String,
    body: String
) : GenericMail("internationallince@gmail.com", to, subject, body)