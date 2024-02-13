package async

import java.util.concurrent.Callable
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

fun sum(a: Int, b: Int): Int = a + b

fun main() {
    val completableFuture: CompletableFuture<Int> = CompletableFuture.supplyAsync {
        Thread.sleep(2000)
        sum(100, 200)
    }
    println("계산 시작")
    completableFuture.thenApplyAsync(::println) // 논블록킹으로 동작

    while (!completableFuture.isDone) {
        Thread.sleep(500)
        println("계산 중")
    }

    println("계산 결과: ${completableFuture.get()}")
}




//fun main() {
//    val pool: ExecutorService = Executors.newSingleThreadExecutor()
//    val future: Future<Int> = pool.submit(Callable { sum(100, 200) })
//    println("계산 시작")
//    val result = future.get() // 비동깅 작업의 결과를 기다린다.
//    println("계산 결과: $result")
//}

//fun main() {
//    val pool: ExecutorService = Executors.newFixedThreadPool(5)
//    try {
//        for (i in 1..10) {
//            pool.execute {
//                println("Thread: ${Thread.currentThread().name}")
//            }
//        }
//    } finally {
//        pool.shutdown()
//    }
//    println("Main Thread: ${Thread.currentThread().name}")
//}