package io.tintoll.userservice.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

//@ConstructorBinding // 에러나는 이유를 모르겠다.
@ConfigurationProperties(prefix = "jwt")
class JWTProperties(
    val issuer: String,
    val subject: String,
    val expiresTime: Long,
    val secret: String,
)
