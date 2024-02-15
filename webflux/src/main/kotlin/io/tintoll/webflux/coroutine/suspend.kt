package io.tintoll.webflux.coroutine

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun doSomething() {
    println("doSomething")
    // test() // 호출 불가
}

suspend fun test() {
    println("test")
}

suspend fun main() = coroutineScope<Unit> {
    println("World!")
    launch {
        delay(100)
        doSomething()
    }

    launch {
        delay(100)
        test()
    }

}