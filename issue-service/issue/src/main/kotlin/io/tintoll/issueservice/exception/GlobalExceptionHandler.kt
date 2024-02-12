package io.tintoll.issueservice.exception

import com.auth0.jwt.exceptions.TokenExpiredException
import mu.KotlinLogging
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(ServerException::class)
    fun handleServerException(e: ServerException): ErrorResponse {
        logger.error { e.message }
        return ErrorResponse(e.code, e.message)
    }

    @ExceptionHandler(TokenExpiredException::class)
    fun handleTokenExpiredException(e: TokenExpiredException): ErrorResponse {
        logger.error { e.message }
        return ErrorResponse(code = 401, message = "Token Expired Error")
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ErrorResponse {
        logger.error { e.message }
        return ErrorResponse(code = 500, message = "Internal Server Error")
    }
}