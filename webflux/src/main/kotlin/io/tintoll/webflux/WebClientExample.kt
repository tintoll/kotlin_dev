package io.tintoll.webflux

import io.tintoll.webflux.books.Book
import org.slf4j.LoggerFactory
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux

@RestController
class WebClientExample {

    val url = "http://localhost:8080/books"
    val log = LoggerFactory.getLogger(this.javaClass)

    @GetMapping("/books/blocking")
    fun getBooksBlocking(): List<Book> {
        log.info("Start RestTemplate")
        val restTemplate = RestTemplate()
        val response =
            restTemplate.exchange(url, HttpMethod.GET, null, object : ParameterizedTypeReference<List<Book>>() {})

        val result = response.body!!
        log.info("reuslt : {}", result)

        log.info("End RestTemplate")
        return result
    }

    @GetMapping("/books/nonblocking")
    fun getBooksNonBlocking(): Flux<Book> {
        log.info("Start WebClient")
        val flux = WebClient.create()
            .get()
            .uri(url)
            .retrieve()
            .bodyToFlux(Book::class.java)
            .map {
                log.info("map : {}", it)
                it
            }
        log.info("End WebClient")
        return flux
    }
}