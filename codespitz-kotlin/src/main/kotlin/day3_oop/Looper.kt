package day3_oop

import kotlinx.datetime.Clock


class Looper(
    private val started: (Looper) -> Unit,
    private val ended: (Looper) -> Unit
) {
    companion object {
        // list 가아닌 set 을 사용하는 이유는 중복을 허용하지 않기 위함(참조주소에대한)
        val users = hashSetOf<User>()
    }
    var isRunning = false
        private set

    fun start() {
        isRunning = true
        started(this)
    }
    fun end() {
        isRunning = false
        ended(this)
    }
}

val threadLooper = Looper(
    {
        val thread = Thread{
            while(it.isRunning && !Thread.currentThread().isInterrupted) {
                val now = Clock.System.now()
                Looper.users.forEach{it.send(now)}
                Thread.sleep(1000)
            }
        }
    },
    {}
)

// 인터페이스를 구현할때는 () 없고 상속할때는 생성자를 호출해야함
//object ThreadLooper : Looper() {
//    private val thread by lazy {
//        Thread {
//            while(isRunning && !Thread.currentThread().isInterrupted) {
//                val now = Clock.System.now()
//                Looper.users.forEach{it.send(now)}
//                Thread.sleep(1000)
//            }
//        }
//    }
//    override fun started() {
//        if(!thread.isAlive) {
//            thread.start()
//        }
//    }
//
//    override fun ended() {
//    }
//}