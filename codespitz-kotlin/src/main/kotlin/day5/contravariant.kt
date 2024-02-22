package day5

internal class Node<in T : Number>(
    private val value: T,
    private val next: Node<T>? = null
) {
    operator fun contains(target: T): Boolean {
        return if (value.toInt() == target.toInt()) true else next?.contains(target) ?: false
    }
    fun isPositive(target: T): Boolean {
        return target.toInt().isPositive() // error, 에러 안나게 하려면 Int의 확장함수를 만들어주어야 한다.
    }
}

// 확장함수 추가
fun Int.isPositive(): Boolean {
    return this >= 0
}

fun main() {
    val node: Node<Int> = Node<Number>(10.0)
    node.contains(8)
}