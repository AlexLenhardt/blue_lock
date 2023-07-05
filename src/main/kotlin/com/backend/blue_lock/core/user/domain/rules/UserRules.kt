package com.backend.blue_lock.core.user.domain.rules

import com.backend.blue_lock.core.user.domain.errors.ERROR_AUTHENTICATION_RECORD_EMPTY
import com.backend.blue_lock.core.user.domain.errors.ERROR_NAME_EMPTY
import com.backend.blue_lock.core.user.domain.errors.ERROR_USER_TYPE_EMPTY
import com.backend.blue_lock.core.user.domain.entities.User
import com.backend.blue_lock.core.user.domain.errors.UserException
import org.springframework.stereotype.Component

@Component
class UserRules {
    fun rules(user: User): UserException? {

        if (user.name.isNullOrBlank()) {
            return ERROR_NAME_EMPTY
        }

        if (user.userType == null) {
            return ERROR_USER_TYPE_EMPTY
        }

        if (user.authenticationRecord.isNullOrBlank()) {
            return ERROR_AUTHENTICATION_RECORD_EMPTY
        }

        return null
    }
}