package io.tintoll.userservice.model

import io.tintoll.userservice.domain.entity.User


data class UserEditRequest(
    val username:String,
)


data class MeResponse(
    val id: Long,
    val email: String,
    val username: String,
    val profileUrl: String?,
    val createdAt: String,
    val updatedAt: String
) {
    companion object {
        operator fun invoke(user: User) = with(user) {
            MeResponse(
                id = id!!,
                email = email,
                username = username,
                profileUrl = if (profileUrl != null) "http://localhost:9090/images/$profileUrl" else null,
                createdAt = createdAt.toString(),
                updatedAt = updatedAt.toString()
            )
        }
    }
}