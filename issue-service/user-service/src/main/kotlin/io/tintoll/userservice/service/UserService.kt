package io.tintoll.userservice.service

import io.tintoll.userservice.domain.SignUpRequest
import io.tintoll.userservice.domain.entity.User
import io.tintoll.userservice.domain.repository.UserRepository
import io.tintoll.userservice.exception.UserExistsException
import io.tintoll.userservice.utils.BCryptUtils
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    suspend fun signUp(singUpRequest: SignUpRequest) {
        with(singUpRequest) {
            userRepository.findByEmail(email)?.let {
                throw UserExistsException()
            }

            val user = User(
                email = email,
                password = BCryptUtils.hash(password),
                username = username,
            )

            userRepository.save(user)
        }
    }
}