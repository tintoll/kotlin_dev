package io.tintoll.webflux.r2dbc

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface BokeRespository : ReactiveCrudRepository<Boke, Long> {
    fun findByName(name: String): Mono<Boke>
}