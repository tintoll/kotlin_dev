package day4

import kotlin.reflect.KProperty

data class Infinity<T>(
    private val value: T,
    private val limit:Int = -1,
    private val nextValue: (T) -> T
) {
    class Iter<T>(private var item: Infinity<T>) : Iterator<T> {
        override fun hasNext() = item.limit != 0

        override fun next(): T {
            // by 의 옆에 오는 item 을 속성 델리게이터 라고 한다
            val result by item
            item = item.next()
            return result
        }
    }

    operator fun iterator() = Iter(this)
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = value
    fun next() = Infinity(nextValue(value), limit-1, nextValue)
}

fun main() {
    val a = Infinity(0,20) { it + 1 }
    for (i in a) {
        println(i)
    }
}