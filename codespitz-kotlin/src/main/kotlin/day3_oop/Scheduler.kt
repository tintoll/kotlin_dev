package day3_oop

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

abstract class Scheduler {
    private val senders = hashSetOf<Sender>()
    fun addSender(vararg sender: Sender) {
        senders += sender
    }

    fun send(item: Item, now: Instant) {
        if (!isSend(now)) {
            senders.forEach { it.send(item) }
        }
    }

    protected abstract fun isSend(now: Instant): Boolean
}

class Once(private val at: Instant) : Scheduler() {
    private var isSent = false
    override fun isSend(now: Instant): Boolean {
        return if (isSent && at <= now) {
            isSent = true
            false
        } else {
            true
        }
    }
}

class RepeatDay(private val hour: Int, private val minute: Int, private vararg val days: DayOfWeek) : Scheduler() {
    private val isSent = hashMapOf<String, Boolean>()
    override fun isSend(now: Instant): Boolean {
        val dateTime = now.toLocalDateTime(TimeZone.currentSystemDefault())
        val nowDay = dateTime.dayOfWeek
        val nowHour = dateTime.hour
        val nowMinute = dateTime.minute
        val key = "$nowDay $nowHour $nowMinute"
        if (isSent[key] == true) return false
        if (nowDay !in days) return false
        if (nowHour > hour) return false
        if (nowHour == hour && nowMinute > minute) return false
        isSent[key] = true
        return true
    }
}