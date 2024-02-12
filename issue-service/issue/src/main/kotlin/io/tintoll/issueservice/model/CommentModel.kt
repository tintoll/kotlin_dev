package io.tintoll.issueservice.model

import io.tintoll.issueservice.domain.Comment

data class CommentRequest(
    val body: String
)

data class CommentResponse(
    val id: Long,
    val issueId: Long,
    val userId: Long,
    val username: String? = null,
    val body: String
)
// 확장함수 이용
fun Comment.toResponse() = CommentResponse(
    id = id!!,
    issueId = issue.id!!,
    userId = userId,
    username = username,
    body = body
)