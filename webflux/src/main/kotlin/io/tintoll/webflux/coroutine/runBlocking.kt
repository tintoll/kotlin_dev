package io.tintoll.webflux.coroutine

import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        printHello()
        println(Thread.currentThread().name)
    }
    println("World!")
    println(Thread.currentThread().name)
}

fun printHello() = println("hello")