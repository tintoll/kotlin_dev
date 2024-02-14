package io.tintoll.webflux.books

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import java.util.concurrent.atomic.AtomicInteger

data class Book(val id: Int, val name: String, val price: Int)

@Service
class BookService {

    private final val nextId = AtomicInteger(0)

    val books = mutableListOf(
        Book(nextId.incrementAndGet(), "Kotlin", 20000),
        Book(nextId.incrementAndGet(), "Spring", 30000),
        Book(nextId.incrementAndGet(), "Java", 15000)
    )

    fun getAll(): Flux<Book> {
//        return Flux.fromIterable(books)
        // 확장함수를 이용해서 쉽게 Flux나 Mono로 변환할 수 있다.
        return books.toFlux()
    }

    fun get(id: Int): Mono<Book> {
//        return Mono.justOrEmpty(books.find { it.id == id })
        return books.find { it.id == id }.toMono()
    }

    fun add(request: Map<String, Any>): Mono<Book> {
        return Mono.just(request)
            .map {
                val id = nextId.incrementAndGet()
                val name = it["name"].toString()
                val price = it["price"] as Int
                val book = Book(id, name, price)
                books.add(book)
                book
            }
    }

    fun delete(id: Int): Mono<Void> {
        return Mono.justOrEmpty(books.find { it.id == id })
            .map {
                books.remove(it)
            }
            .then()
    }
}