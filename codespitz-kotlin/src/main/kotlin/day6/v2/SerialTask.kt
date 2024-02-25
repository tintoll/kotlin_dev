package day6.v2

import java.util.concurrent.Executors

enum class Stat {
    READY, MARK, CONFIRM
}

class Task internal constructor(val run: (Controller) -> Unit) {
    internal var isStarted = Stat.READY
    internal var isCompleted = Stat.READY
    internal var result: Result<Any?>? = null
    internal var next: Task? = null
}

class Controller internal constructor(private val task: Task) {
    val data get() = task.result
    fun cancel(throwable: Throwable) {
        task.next?.result = Result.failure(throwable)
        task.isCompleted = Stat.MARK
    }

    fun resume(data: Any? = null) {
        task.next?.result = Result.success(data)
        task.isCompleted = Stat.MARK
    }
}

class SerialTask(private val dispatcher: Dispatcher, vararg block: (Controller) -> Unit) : Runnable {
    private var task: Task

    init {
        if (block.isEmpty()) throw Throwable("no blocks")
        var prev = Task(block[0])
        task = prev
        prev.isStarted = Stat.MARK
        for (i in 1..block.lastIndex) {
            val task = Task(block[i])
            prev.next = task
            prev = task
        }
    }

    override fun run() {
        while (!Thread.currentThread().isInterrupted) {
            Thread.sleep(5)
            if (task.isCompleted == Stat.MARK) {
                task.next?.let {
                    it.isStarted = Stat.MARK
                    task = it
                }
            }
            if (task.isStarted == Stat.MARK) {
                task.run(Controller(task))
                task.isStarted = Stat.CONFIRM
            }
        }
    }

    fun launch() {
        dispatcher.start(this)
    }
}

interface Dispatcher {
    fun start(looper: SerialTask)
    fun join()
    class FixedDispatcher(private val threads: Int) : Dispatcher {
        private val executor = Executors.newFixedThreadPool(threads)
        override fun start(serialTask: SerialTask) {
            executor.execute(serialTask)
        }

        override fun join() {
            while (!executor.isShutdown) {
            }
        }
    }
}

fun main() {
    val dispatcher = Dispatcher.FixedDispatcher(10)
    for (i in 1..5) {
        val looper = SerialTask(dispatcher, {
            println("$i-0 ${Thread.currentThread().id}")
            it.resume()
        }, {
            println("$i-1 ${Thread.currentThread().id}")
            it.resume()
        }, {
            println("$i-2 ${Thread.currentThread().id}")
            it.resume()
        })
        looper.launch()
    }
    dispatcher.join()
}