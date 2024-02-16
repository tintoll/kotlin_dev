package io.tintoll.issueservice.config

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport

@Configuration
class WebConfig(
    private val authUserHandlerArgumentResolver: AuthUserHandlerArgumentResolver,
) : WebMvcConfigurationSupport() {
    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
        argumentResolvers.apply {
            add(authUserHandlerArgumentResolver)
        }
    }
}

@Component
class AuthUserHandlerArgumentResolver(@Value("\${auth.url}") private val authUrl: String) :
    HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean =
        AuthUser::class.java.isAssignableFrom(parameter.parameterType)

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?
    ): Any? {

        // 헤더에서 token 가져옴
        val authHeader = webRequest.getHeader("Authorization")

        // override 하는 메서드가 suspend가 아니기 때문에 runBlocking을 이용하여 코루틴스코프를 만들어줌.
        return runBlocking {
            WebClient.create()
                .get()
                .uri(authUrl)
                .header("Authorization", authHeader)
                .retrieve()
                .awaitBody<AuthUser>()
        }
    }
}

data class AuthUser(
    @JsonProperty("id")
    val userId: Long,
    val username: String,
    val email: String,
    val profileUrl: String? = null
)