package io.tintoll.userservice.utils

import com.auth0.jwt.interfaces.DecodedJWT
import io.tintoll.userservice.config.JWTProperties
import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class JWTUtilsTest {
    private val logger = KotlinLogging.logger {}
    @Test
    fun createToken() {
        // given
        val claim = JWTClaim(1, "dev@gmail.com", "profile.jpg", "개발자")
        var properties = JWTProperties("java", "auth", 3600, "my-secret")

        // when
        val token = JWTUtils.createToken(claim, properties)

        // then
        assertNotNull(token)

        logger.info { "token: $token" }
    }

    @Test
    fun decode() {
        // given
        val claim = JWTClaim(1, "dev@gmail.com", "profile.jpg", "개발자")
        var properties = JWTProperties("java", "auth", 3600, "my-secret")

        val token = JWTUtils.createToken(claim, properties)

        // when
        val decode: DecodedJWT = JWTUtils.decide(token, properties.secret, properties.issuer)

        // then
        with(decode) {
            logger.info { "claims: $claims" }
            assertEquals(claim.userId, claims["userId"]!!.asLong())
            assertEquals(claim.email, claims["email"]!!.asString())
            assertEquals(claim.profileUrl, claims["profileUrl"]!!.asString())
            assertEquals(claim.username, claims["username"]!!.asString())
        }
    }

}