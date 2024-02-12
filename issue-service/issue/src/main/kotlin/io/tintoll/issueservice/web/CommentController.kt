package io.tintoll.issueservice.web

import io.tintoll.issueservice.config.AuthUser
import io.tintoll.issueservice.model.CommentRequest
import io.tintoll.issueservice.model.CommentResponse
import io.tintoll.issueservice.service.CommentService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/issues/{issueId}/comments")
class CommentController(private val commentService: CommentService) {

    @PostMapping
    fun create(authUser: AuthUser, @PathVariable issueId: Long, @RequestBody request: CommentRequest): CommentResponse {
        return commentService.create(issueId, authUser.userId, authUser.username, request)
    }

    @PutMapping("/{id}")
    fun edit(authUser: AuthUser, @PathVariable id: Long, @RequestBody request: CommentRequest) = commentService.edit(id,authUser.userId, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(authUser: AuthUser,@PathVariable issueId: Long,  @PathVariable id: Long) {
        commentService.delete(issueId, id, authUser.userId)
    }

}