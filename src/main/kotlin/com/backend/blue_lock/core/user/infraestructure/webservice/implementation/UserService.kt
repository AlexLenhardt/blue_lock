package com.backend.blue_lock.core.user.infraestructure.webservice.implementation

import com.backend.blue_lock.core.user.security.SystemUser
import com.backend.blue_lock.core.user.domain.entities.UserType
import com.backend.blue_lock.core.user.domain.entities.User
import com.backend.blue_lock.core.user.domain.usecases.SecurityUseCase
import com.backend.blue_lock.core.user.domain.usecases.UserUseCase
import com.backend.blue_lock.core.shared.entities.BasicFilter
import com.backend.blue_lock.core.user.domain.entities.PasswordChangeRequest
import com.backend.blue_lock.core.user.domain.entities.PasswordChangeResponse
import com.backend.blue_lock.core.user.domain.entities.EnumUserType
import com.backend.blue_lock.core.user.domain.errors.INVALID_CREDENTIALS
import com.backend.blue_lock.core.user.domain.errors.USER_STORAGE_ERROR
import com.backend.blue_lock.core.user.domain.usecases.response.UserResponse
import com.backend.blue_lock.core.user.domain.usecases.response.*
import com.backend.blue_lock.core.user.infraestructure.webservice.UserService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserService(
    private val userUseCase: UserUseCase,
    private val securityUseCase: SecurityUseCase,
    private val authenticationManager: AuthenticationManager,
) : UserService {
    @PostMapping("/register")
    override fun autoRegister(@RequestBody user: User): UserResponse? {
        return userUseCase.postUser(user.copy(userType = UserType(code = EnumUserType.User.value)))
    }

    @GetMapping("/{userID}")
    override fun getUser(@PathVariable("userID") userID: String): UserResponse = userUseCase.getUser(userID)

    @PostMapping("/new/password/{authenticationRecord}")
    override fun generateNewPassword(
        @PathVariable("authenticationRecord") authenticationRecord: String,
    ): GeneratePasswordResponse =
        securityUseCase.generateNewPassword(authenticationRecord)

    @PostMapping("/updatePassword")
    override fun updatePassword(@RequestBody request: PasswordChangeRequest): PasswordChangeResponse {
        return try {
           val user = SecurityContextHolder.getContext().authentication.principal as SystemUser

           authenticationManager
               .authenticate(UsernamePasswordAuthenticationToken((user).uuid, request.currentPassword))

            securityUseCase.updateUserPassword(request)
        } catch (e: AuthenticationException) {
            PasswordChangeResponse(false, INVALID_CREDENTIALS)
        }
    }


    @PreAuthorize("hasAuthority('USER_WRITE')")
    @PostMapping
    override fun postUser(@RequestBody user: User): UserResponse = userUseCase.postUser(user)

    @GetMapping("/types")
    override fun listUserType(): UserTypeResponse = userUseCase.listUserType()

    @GetMapping
    override fun listAllUsers(
        @RequestParam("page", required = false, defaultValue = "0") page: Int,
        @RequestParam("size", required = false, defaultValue = "30") size: Int,
        @RequestParam("orderBy", required = false, defaultValue = "") orderBy: String,
        @RequestParam("sortBy", required = false, defaultValue = "asc") sortBy: String,
        @RequestParam("filter", required = false) filter: List<BasicFilter>?,
    ): UserListAllResponse {
        return userUseCase.listAllUsers(
            page = page,
            size = size,
            orderBy = orderBy,
            sortBy = sortBy,
            filters = filter?.map { UserFilter(it.name, it.value) },
        )
    }

}