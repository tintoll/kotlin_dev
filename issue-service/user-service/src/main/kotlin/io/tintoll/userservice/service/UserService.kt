package io.tintoll.userservice.service

import io.tintoll.userservice.config.JWTProperties
import io.tintoll.userservice.domain.SignInRequest
import io.tintoll.userservice.domain.SignInResponse
import io.tintoll.userservice.domain.SignUpRequest
import io.tintoll.userservice.domain.entity.User
import io.tintoll.userservice.domain.repository.UserRepository
import io.tintoll.userservice.exception.InvalidJwtTokenException
import io.tintoll.userservice.exception.PasswordNotMatchedException
import io.tintoll.userservice.exception.UserExistsException
import io.tintoll.userservice.exception.UserNotFoundException
import io.tintoll.userservice.utils.BCryptUtils
import io.tintoll.userservice.utils.JWTClaim
import io.tintoll.userservice.utils.JWTUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import kotlin.time.Duration

@Service
class UserService(
    private val userRepository: UserRepository,
    private val jwtProperties: JWTProperties,
    private val cacheManager: CoroutineCacheManager<User>
) {

    companion object {
        private val CACHE_TTL = java.time.Duration.ofMinutes(1)
    }

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

    suspend fun signIn(signInRequest: SignInRequest): SignInResponse {
        return with(userRepository.findByEmail(signInRequest.email) ?: throw UserNotFoundException()) {
            val verified = BCryptUtils.verify(signInRequest.password, password)
            if (!verified) {
                throw PasswordNotMatchedException()
            }

            val jwtClaim = JWTClaim(userId = id!!, email = email, username = username, profileUrl = profileUrl)

            val token = JWTUtils.createToken(jwtClaim, jwtProperties)
            cacheManager.awaitPut(key = token, value = this, ttl = CACHE_TTL)
            SignInResponse(email = email, username = username, token = token)
        }

    }

    suspend fun logout(token: String) {
        cacheManager.awaitEvict(token)
    }

    suspend fun getByToken(token: String): User {
        val cachedUser: User = cacheManager.awaitGetOrPut(key = token, ttl = CACHE_TTL) {
            val decodedJWT = JWTUtils.decode(token, jwtProperties.secret, jwtProperties.issuer)

            val userId = decodedJWT.claims["userId"]?.asLong() ?: throw InvalidJwtTokenException()
            get(userId)
        }
        return cachedUser
    }

    suspend fun get(userId: Long): User {
        return userRepository.findById(userId) ?: throw UserNotFoundException()
    }

}