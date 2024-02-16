package io.tintoll.userservice.controller

import io.tintoll.userservice.domain.SignInRequest
import io.tintoll.userservice.domain.SignInResponse
import io.tintoll.userservice.domain.SignUpRequest
import io.tintoll.userservice.model.AuthToken
import io.tintoll.userservice.model.MeResponse
import io.tintoll.userservice.model.UserEditRequest
import io.tintoll.userservice.service.UserService
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.*
import java.io.File

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

    // 이미지 파일을 받으려고 MultiPartForm 을 사용하고 있음.
    @PostMapping("/{id}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    suspend fun edit(@PathVariable id: Long,
                     @ModelAttribute userEditRequest: UserEditRequest,
                     @AuthToken token: String,
                     @RequestPart("profileUrl") fiePart: FilePart
    ) {
        val orgFilename = fiePart.filename()
        var filename : String? = null
        if(orgFilename.isNotEmpty()) {
            val ext = orgFilename.substring(orgFilename.lastIndexOf(".") + 1)
            filename = "${id}.${ext}"
            val file = File(ClassPathResource("/images").file, filename)
            fiePart.transferTo(file).awaitFirstOrNull()
        }

        userService.edit(token, userEditRequest.username, filename)
    }
}