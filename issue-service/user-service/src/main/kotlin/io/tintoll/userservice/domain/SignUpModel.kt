package io.tintoll.userservice.domain

data class SignUpRequest(
    val email: String,
    val password: String,
    val username: String,
)
