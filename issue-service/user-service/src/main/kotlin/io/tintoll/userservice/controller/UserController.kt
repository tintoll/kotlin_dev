package io.tintoll.userservice.controller

import io.tintoll.userservice.domain.SignInRequest
import io.tintoll.userservice.domain.SignInResponse
import io.tintoll.userservice.domain.SignUpRequest
import io.tintoll.userservice.model.AuthToken
import io.tintoll.userservice.model.MeResponse
import io.tintoll.userservice.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController (private val userService: UserService){

    @PostMapping("/signup")
    suspend fun signUp(@RequestBody signUpRequest: SignUpRequest) {
        userService.signUp(signUpRequest)
    }

    @PostMapping("/signin")
    suspend fun signIn(@RequestBody signInRequest: SignInRequest) : SignInResponse {
        return userService.signIn(signInRequest)
    }

    @DeleteMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun logout(@AuthToken token: String) {
        userService.logout(token)
    }

    @GetMapping("/me")
    suspend fun get(@AuthToken token: String) : MeResponse {
        return MeResponse(userService.getByToken(token))
    }

    @GetMapping("/{userId}/username")
    suspend fun getUsername(userId: Long) : Map<String, String> {
        return mapOf("reporter" to userService.get(userId).username)
    }
}