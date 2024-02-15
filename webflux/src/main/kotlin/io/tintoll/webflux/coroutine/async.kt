package io.tintoll.webflux.coroutine

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

fun sum(a: Int, b: Int) = a + b

fun main() = runBlocking<Unit> {
    val result1: Deferred<Int> = async {
        println("async1")
        sum(1, 2)
    }

    val result2 = async {
        println("async2")
        sum(3, 4)
    }

    println("Result: ${result1.await() + result2.await()}")
}