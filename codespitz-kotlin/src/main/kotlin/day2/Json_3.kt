package day2

import kotlin.reflect.KProperty

//private fun <T : Any> jsonObject(target: T): String {
//    val builder = StringBuilder()
//    return target::class.members.filterIsInstance<KProperty<*>>().joinTo(builder, ",", "{", "}") {
//        val value = it.getter.call(target)
//        "${stringify(it.name)}:${stringify(it.getter.call(target))}"
//    }.toString()
//}
//
//private fun stringify(value: Any?): String = when (value) {
//    null -> "null"
//    is String -> jsonString(value)
//    is Boolean, is Number -> "$value"
//    is List<*> -> jsonList(value)
//    else -> jsonObject(value)
//}
//
//private fun jsonList(target: List<*>): String {
//    val builder = StringBuilder()
//    return target.joinTo(
//        builder,
//        ",", "[", "]",
//        transform = ::stringify
//    ).toString()
//}
//
//// 문자열이면 감싸는 따옴표 붙임
//// 문자열안에 따옴표는 \"로 변경
//private fun jsonString(v: String) = """"${v.replace("\"", "\\\"")}""""
//class Json3(val a: Int, val b: String, val c: List<String>)
//
//fun main() {
//    val json = Json3(10, "hello", listOf("a", "b", "c"))
//    println(stringify(json))
//}