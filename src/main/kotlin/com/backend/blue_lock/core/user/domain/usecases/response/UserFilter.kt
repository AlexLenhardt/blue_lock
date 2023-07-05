package com.backend.blue_lock.core.user.domain.usecases.response

class UserFilter(
    val name: String,
    val value: String,
) {
    override fun toString(): String {
        return "UserFilters=($name, $value)"
    }
}
