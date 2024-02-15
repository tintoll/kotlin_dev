package io.tintoll.userservice.config

import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator

@Configuration
class R2DBCConfig {

    // 테이블 초기화 작업
    @Bean
    fun init(connectionFactory: ConnectionFactory) = ConnectionFactoryInitializer().apply {
        setConnectionFactory(connectionFactory)
        setDatabasePopulator(
            ResourceDatabasePopulator(
                ClassPathResource("scripts/schema.sql")
            )
        )
    }


}