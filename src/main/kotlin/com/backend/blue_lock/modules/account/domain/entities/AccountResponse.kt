package com.backend.blue_lock.modules.account.domain.entities

import com.backend.blue_lock.modules.account.domain.entities.Account
import com.backend.blue_lock.core.shared.error.GenericError

class AccountResponse(
    val account: Account? = null,
    val error: GenericError? = null
)