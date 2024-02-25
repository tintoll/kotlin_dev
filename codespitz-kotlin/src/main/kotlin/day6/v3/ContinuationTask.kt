package day6.v3


import java.util.concurrent.Executors

enum class Stat {
    READY, MARK, CONFIRM
}

class Task internal constructor(internal val run: (Continuation) -> Unit) {
    internal var continuation = Continuation(this)
    internal var isStarted = Stat.READY
    internal var isCompleted = Stat.READY
    internal var env: MutableMap<String, Any?> = hashMapOf()
}

class Continuation internal constructor(private val task: Task) {
    var step = 0
        private set

    operator fun get(key: String): Any? = task.env[key]
    operator fun set(key: String, value: Any?) {
        task.env[key] = value
    }

    internal var failed: Throwable? = null
    fun cancel(throwable: Throwable) {
        failed = Throwable("step:$step, env:${task.env}", throwable)
        task.isCompleted = Stat.MARK
    }

    fun complete() {
        task.isCompleted = Stat.MARK
    }

    fun resume(step: Int) {
        this.step = step
        task.isStarted = Stat.READY
    }
}


class ContinuationTask(
    private val dispatcher: Dispatcher,
    isLazy: Boolean,
    block: (Continuation) -> Unit
) : Runnable {
    private val task = Task(block)

    init {
        if (!isLazy) launch()
    }

    override fun run() {
        while (!Thread.currentThread().isInterrupted) {
            Thread.sleep(5)
            if (task.isCompleted == Stat.MARK) break
            if (task.isStarted == Stat.READY) {
                task.isStarted = Stat.CONFIRM
                task.run(task.continuation)
            }
        }
        task.continuation.failed?.let {
            throw it
        }
    }

    fun launch() {
        dispatcher.start(this)
    }
}

interface Dispatcher {
    fun start(continuationTask: ContinuationTask)
    fun join()
    class FixedDispatcher(private val threads: Int) : Dispatcher {
        private val executor = Executors.newFixedThreadPool(threads)
        override fun start(continuationTask: ContinuationTask) {
            executor.execute(continuationTask)
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
        ContinuationTask(dispatcher, false) {
            when (it.step) {
                0 -> {
                    println("$i-0 ${Thread.currentThread().id}")
                    it.resume(1)
                }

                1 -> {
                    println("$i-1 ${Thread.currentThread().id}")
                    it.resume(2)
                }

                2 -> {
                    println("$i-2 ${Thread.currentThread().id}")
                    it.complete()
                }
            }

        }
    }
    dispatcher.join()
}