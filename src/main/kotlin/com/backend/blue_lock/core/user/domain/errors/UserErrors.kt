package com.backend.blue_lock.core.user.domain.errors

import com.backend.blue_lock.core.shared.error.GenericError

class UserException(
    code: String,
    description: String
) : GenericError("user-module", code, description)

val USER_STORAGE_ERROR =
    UserException("USER_STORAGE_ERROR", "An error has occurred on user storage")

val USER_NOT_FOUND =
    UserException("USER_NOT_FOUND", "User not found")

val USER_EMAIL_EXIST =
    UserException("USER_EMAIL_EXIST", "E-mail already used")

val ERROR_NAME_EMPTY =
    UserException("ERROR_NAME_EMPTY", "Name cannot be empty")

val IDENTIFIER_CANNOT_BE_EMPTY =
    UserException("IDENTIFIER_CANNOT_BE_EMPTY", "Identifier cannot be empty")

val ERROR_USER_TYPE_EMPTY =
    UserException("ERROR_USER_TYPE_EMPTY", "User type cannot be empty")

val ERROR_USER_TYPE_NOT_FOUND =
    UserException("ERROR_USER_TYPE_NOT_FOUND", "User type not found")

val ERROR_AUTHENTICATION_RECORD_EMPTY =
    UserException("ERROR_AUTHENTICATION_RECORD_EMPTY", "Authentication record cannot be empty")

val AUTHENTICATION_RECORD_ALREADY_EXIST =
    UserException("AUTHENTICATION_RECORD_ALREADY_EXIST", "Authentication record already exist")

val ACCESS_DENIED =
    UserException("ACCESS_DENIED", "User don't have access to this action")