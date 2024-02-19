package day3_oop

import kotlinx.datetime.Instant

class Item(var title:String, var content: String) {
    private val schedules = hashSetOf<Scheduler>()
    fun addSchedule(vararg schedule: Scheduler) {
        schedules += schedule
    }
    fun send(now: Instant) {
        schedules.forEach{it.send(this,now)}
    }
}