package io.tintoll.webflux.coroutine

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

fun main() = runBlocking<Unit> {
    val job1: Job = launch {
        // 시간을 측정하는 함수
        val elapsedTime = measureTimeMillis {
            // 스레드를 차단하지 않고 일시 중지 시킨다.
            delay(100)
        }
        println("hello $elapsedTime")
    }
    job1.cancel() // job1을 취소한다.

    // 시작을 지연시킨다.
    val job2: Job = launch(start = CoroutineStart.LAZY) {
        val elapsedTime = measureTimeMillis {
            delay(200)
        }
        println("world $elapsedTime")
    }

    println("Start")
    job2.start() // job2를 시작한다.
}