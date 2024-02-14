package io.tintoll.webflux

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
class Router {
    @Bean
    fun helloRouter(handler: HelloHandler): RouterFunction<ServerResponse> =
        route().GET("/", handler::sayHello).build()

    @Bean
    fun userRouter(handler: UserHandler): RouterFunction<ServerResponse>  =
        router {
            // 중첩 라우터 : 중복된 경로를 그룹화
            "/users".nest {
                GET("", handler::getAll)
                GET("/{id}", handler::getUser)
            }
        }
}