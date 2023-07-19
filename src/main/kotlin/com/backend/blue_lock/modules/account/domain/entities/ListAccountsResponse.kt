package com.backend.blue_lock.modules.account.domain.entities

import com.backend.blue_lock.core.shared.error.GenericError

class ListAccountsResponse(
    val accounts: List<Account>? = null,
    val error: GenericError? = null
)