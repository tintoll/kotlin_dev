package io.tintoll.webflux.coroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit> {
    val flow = sample()
    flow.collect { value -> println(value) }
}

fun sample() : Flow<Int> = flow {
    for (i in 1..3) {
        delay(100)
        emit(i)
    }
}