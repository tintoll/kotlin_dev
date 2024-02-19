package day2

import kotlin.reflect.KProperty
import kotlin.reflect.full.findAnnotation


@Target(AnnotationTarget.PROPERTY)
annotation class Ex // 제외할 속성

@Target(AnnotationTarget.PROPERTY)
annotation class Name(val name:String) // json용 별도 이름 지정



// 확장함수 만든다
fun <T> Iterable<T>.joinTo(sep: () -> Unit, transform: (T) -> Unit) {
    forEachIndexed { count, element ->
        if (count > 0) sep()
        transform(element)
    }
}


fun stringify(value: Any) = StringBuilder().run {
    jsonValue(value)
    toString()
}

// StringBuilder 확장함수로 builder가 여러 함수에서 사용되는 부분을 없앨수 있다.
fun StringBuilder.comma() {
    append(",")
}

fun StringBuilder.wrap(begin:Char, enc:Char, block:StringBuilder.()->Unit) {
    append(begin)
    block()
    append(enc)
}

private fun StringBuilder.jsonValue(value: Any?) {
    when (value) {
        null -> append("null")
        is String -> append(jsonString(value))
        is Boolean, is Number -> append("$value")
        is List<*> -> jsonList(value)
        else -> jsonObject(value)
    }
}

private fun jsonString(v: String) = """"${v.replace("\"", "\\\"")}""""
private fun StringBuilder.jsonObject(target: Any) {
    wrap('{', '}') {
        target::class.members.filterIsInstance<KProperty<*>>()
            .filter{ it.findAnnotation<Ex>() == null}
            .joinTo(::comma) {
            jsonValue(it.findAnnotation<Name>()?.name ?: it.name)
            append(':')
            jsonValue(it.getter.call(target))
        }
    }
}

private fun StringBuilder.jsonList(target: List<*>) {
    wrap('[', ']') {
        target.joinTo(::comma) {
            jsonValue(it)
        }
    }
}

class Json2(@Ex val a:Int,@Name("title") val b: String)
class Json3(val a: Int, val b: String, val c: List<String>)

fun main() {
//    val json = Json3(10, "hello", listOf("a", "b", "c"))
    val json = Json2(10, "hello")
    println(stringify(json))
}