package io.tintoll.webflux

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
//
//fun main() {
//    val mono = Mono.just("Hello, Mono")
//    mono.subscribe(::println)
//    println(mono)
//}

data class Cellphone(
    val name: String,
    val price: Int,
    val currency: Currency,
)
enum class Currency {
    KRW, USD
}
fun main() {
    val iphone =
        Cellphone(name = "Iphone", price = 100, currency = Currency.KRW)
    val galaxy =
        Cellphone(name = "Galaxy", price = 90, currency = Currency.KRW)
    val flux: Flux<Cellphone> =
        Flux.just(iphone, galaxy)
    flux.subscribe(::println)
}