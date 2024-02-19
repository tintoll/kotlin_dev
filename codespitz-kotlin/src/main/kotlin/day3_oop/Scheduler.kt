package day3_oop

import kotlinx.datetime.Instant

abstract class Scheduler {
    private val senders = hashSetOf<Sender>()
    fun addSender(vararg sender: Sender) {
        senders += sender
    }

    fun send(item: Item, now: Instant) {
        if (!isSend(now) senders.forEach { it.send(item) }
    }

    protected abstract fun isSend(now: Instant): Boolean
}