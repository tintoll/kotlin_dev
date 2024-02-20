package day3_oop

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.plus

fun reminder(username:String) {
    threadLooper.start()
    val user = User(username)
    do {
        println("add item")
        val item = Item(read("title: "), read("content: "))
        readN("add schedule", "1.once 2.repeatDay", "1", "2") {
//            val scheduler = getScheduler(it)
//            setSender(scheduler)
//            item.addSchedule(scheduler)
            // 축약
            item.addSchedule(getScheduler(it).also { scheduler -> setSender(scheduler) })
        }
        user.addItem(item)
    } while (true)
    while (threadLooper.isRunning) {
    }

}

fun getScheduler(type: String): Scheduler {
    return when (type) {
        "1" -> getOnce()
        "2" -> getRepeatDay()
        else -> throw Throwable()
    }
}

fun setSender(scheduler: Scheduler) {
    readN("add sender", "1.print 2.gmail", "1", "2") {
        scheduler.addSender(
            when (it) {
                "1" -> PrintSender()
                "2" -> GmailSender(read("user: "), read("password: "), read("sender: "), read("receiver: "))
                else -> throw Throwable()
            }
        )
    }

}

fun getOnce(): Scheduler {
    println("once from now")
    println("unit:")
    val unit: DateTimeUnit.TimeBased = when (read("1.hour 2.minute 3.second", "1", "2", "3")) {
        "1" -> DateTimeUnit.HOUR
        "2" -> DateTimeUnit.MINUTE
        "3" -> DateTimeUnit.SECOND
        else -> throw Throwable()
    }
    val count = read("count(int): ") { it.toIntOrNull() != null }.toInt()
    return Once(Clock.System.now().plus(count, unit))
}

fun getRepeatDay(): Scheduler {
    println("repeat day")
    val time = read("time(hh:mm):") { """^\d{2}:\d{2}$""".toRegex().matches(it) }.split(":").map { it.toInt() }
    return RepeatDay(time[0], time[1], *read("days(mon,tue,wed,thu,fri,sat,sun):") {
        it.split(',').all { el -> "mon,tue,wed,thu,fri,sat,sun".indexOf(el.trim()) != -1 }
    }.split(',').map {
        when (it) {
            "mon" -> DayOfWeek.MONDAY
            "tue" -> DayOfWeek.TUESDAY
            "wed" -> DayOfWeek.WEDNESDAY
            "thu" -> DayOfWeek.THURSDAY
            "fri" -> DayOfWeek.FRIDAY
            "sat" -> DayOfWeek.SATURDAY
            "sun" -> DayOfWeek.SUNDAY
            else -> throw Throwable()
        }
    }.toTypedArray())
}

fun main() {
    reminder("user")
}