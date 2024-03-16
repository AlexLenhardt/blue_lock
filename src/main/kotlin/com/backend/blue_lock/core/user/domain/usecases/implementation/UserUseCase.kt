package com.backend.blue_lock.core.user.domain.usecases.implementation

import com.backend.blue_lock.core.user.domain.entities.User
import com.backend.blue_lock.core.user.domain.rules.UserRules
import com.backend.blue_lock.core.user.domain.repository.UserRepository
import com.backend.blue_lock.core.user.domain.usecases.UserUseCase
import com.backend.blue_lock.core.user.domain.errors.*
import com.backend.blue_lock.core.user.domain.usecases.response.UserFilter
import com.backend.blue_lock.core.user.domain.usecases.response.UserListAllResponse
import com.backend.blue_lock.core.user.domain.usecases.response.UserResponse
import com.backend.blue_lock.core.user.domain.usecases.response.UserTypeResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*
import kotlin.math.ceil

@Component
class UserUseCase(
    private val userRepository: UserRepository,
    private val rules: UserRules,
    private val securityUseCase: SecurityUseCase,
) : UserUseCase {
    private val logger = LoggerFactory.getLogger(UserUseCase::class.java)

    override fun getUser(userID: String): UserResponse {
        if (userID.trim() == "") {
            return UserResponse(error = IDENTIFIER_CANNOT_BE_EMPTY)
        }

        try {
            val userByEmail = userRepository.getUserByEmail(userID)
            if (userByEmail != null) {
                return UserResponse(user = userByEmail)
            }
        } catch (e: Exception) {
            return UserResponse(error = USER_STORAGE_ERROR)
        }

        try {
            val userByAuthenticationRecord = userRepository.getUserByAuthenticationRecord(userID)
            if (userByAuthenticationRecord != null) {
                return UserResponse(user = userByAuthenticationRecord)
            }
        } catch (e: Exception) {
            return UserResponse(error = USER_STORAGE_ERROR)
        }

        try {
            val userByUUID = userRepository.getUserByUUID(UUID.fromString(userID))
            if (userByUUID != null) {
                return UserResponse(user = userByUUID)
            }
        } catch (ignored: IllegalArgumentException) {
            // we can ignore this exception
        }

        return UserResponse(error = USER_NOT_FOUND)
    }

    override fun postUser(user: User): UserResponse {
        val userRulesReturn = rules.rules(user)
        if (userRulesReturn != null) {
            return UserResponse(error = userRulesReturn)
        }

        try {
            println(user.userType)
            if (user.userType == null) {
                return UserResponse(error = ERROR_USER_TYPE_NOT_FOUND)
            }

            val newUserType = userRepository.getUserType(user.userType!!)
            if (newUserType == null) {
                return UserResponse(error = ERROR_USER_TYPE_NOT_FOUND)
            }
            user.userType = newUserType
        } catch (e: Exception) {
            println(e.message)
            return UserResponse(error = USER_STORAGE_ERROR)
        }

        if (!user.email.isNullOrBlank()) {
            try {
                val userByEmail = userRepository.getUserByEmail(user.email)
                if (userByEmail != null && userByEmail.uuid != user.uuid) {
                    return UserResponse(error = USER_EMAIL_EXIST)
                }
            } catch (e: Exception) {
                return UserResponse(error = USER_STORAGE_ERROR)
            }
        }

        try {
            val userByAuthenticationRecord: User? =
                userRepository.getUserByAuthenticationRecord(user.authenticationRecord!!)
            if (userByAuthenticationRecord != null && userByAuthenticationRecord.uuid != user.uuid) {
                return UserResponse(error = AUTHENTICATION_RECORD_ALREADY_EXIST)
            }
        } catch (e: Exception) {
            return UserResponse(error = USER_STORAGE_ERROR)
        }

        if (user.uuid != null) {
            return try {
                UserResponse(userRepository.updateUser(user))
            } catch (e: Exception) {
                UserResponse(error = USER_STORAGE_ERROR)
            }
        }

        val createdUser = securityUseCase.createUser(user)

        return UserResponse(user = createdUser.copy(passwordHash = null))
    }

    override fun listUserType(): UserTypeResponse {
        return try {
            UserTypeResponse(userTypes = userRepository.listUserType())
        } catch (e: Exception) {
            UserTypeResponse(error = USER_STORAGE_ERROR)
        }
    }

    override fun listAllUsers(
        page: Int,
        size: Int,
        orderBy: String,
        sortBy: String,
        filters: List<UserFilter>?
    ): UserListAllResponse {
        return try {
            var totalUsers = userRepository.getCountUsers(filters)
            var totalPages = 0
            var firstItem = 0

            if (totalUsers != 0) {
                totalPages = ceil(totalUsers.toDouble() / size).toInt()

                if (totalPages == 0) {
                    totalPages = 1
                }
                if (page != 0) {
                    firstItem = (page - 1) * size
                }
            }

            UserListAllResponse(
                users = userRepository.listAllUsers(firstItem, size, orderBy, sortBy, filters),
                page = page,
                size = totalUsers,
                numberPages = totalPages
            )
        } catch (e: Exception) {
            logger.error("storage error reading users", e)

            UserListAllResponse(error = USER_STORAGE_ERROR)
        }
    }
}