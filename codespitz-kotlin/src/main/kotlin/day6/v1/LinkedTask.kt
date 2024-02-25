package day6.v1

import java.util.LinkedList
import java.util.Queue
import java.util.concurrent.Executors


class Task(val run: (Controller) -> Unit) {
    var isCompleted = false
    var result: Result<Any?>? = null
    var next: Task? = null
}

class Controller internal constructor(private val task: Task) {
    val data get() = task.result
    fun cancel(throwable: Throwable) {
        task.next?.result = Result.failure(throwable)
        task.isCompleted = true
    }

    fun resume(data: Any? = null) {
        task.next?.result = Result.success(data)
        task.isCompleted = true
    }
}

class EventLooper(private val dispatcher: Dispatcher) : Runnable {
    private val tasks: Queue<Task> = LinkedList()
    private var currTask: Task? = null

    fun linkedTask(vararg blocks: (Controller) -> Unit) {
        if (blocks.isEmpty()) return
        synchronized(tasks) {
            var prev = Task(blocks[0])
            tasks.add(prev)
            for (i in 1..blocks.lastIndex) {
                val task = Task(blocks[i])
                prev.next = task
                prev = task
            }
        }
    }

    override fun run() {
        while (!Thread.currentThread().isInterrupted) {
            Thread.sleep(16)
            synchronized(this) {
                if (currTask != null) {
                    currTask?.let { curr ->
                        if (curr.isCompleted) {
                            curr.next?.let {
                                tasks.add(it)
                            }
                            currTask = null
                        }
                    }
                } else tasks.poll()?.let {
                    currTask = it
                    it.run(Controller(it))
                }
            }
        }
    }
    fun launch(){
        dispatcher.start(this)
    }
    fun join(){
        dispatcher.join()
    }
}

interface Dispatcher{
    fun start(looper: EventLooper)
    fun join()
    class FixedDispatcher(private val threads:Int):Dispatcher {
        private val executor = Executors.newFixedThreadPool(threads)
        override fun start(looper: EventLooper) {
            for(i in 1..threads){
                executor.execute(looper)
            }
        }

        override fun join() {
            while(!executor.isShutdown){
            }
        }
    }
}

fun main() {
    val looper = EventLooper(Dispatcher.FixedDispatcher(4))
    for(i in 0..5) {
        looper.linkedTask(
            {
                println("$i-0 ${Thread.currentThread().id}")
                it.resume()
            },
            {
                println("$i-1 ${Thread.currentThread().id}")
                it.resume()
            },
            {
                println("$i-2 ${Thread.currentThread().id}")
                it.resume()
            }
        )
    }

    looper.launch()
    looper.join()
}