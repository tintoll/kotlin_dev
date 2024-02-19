package day2

import kotlin.reflect.KProperty

//fun <T:Any> stringify(target:T):String {
//    val builder = StringBuilder()
//    return target::class.members.filterIsInstance<KProperty<*>>().
//    joinTo(builder, ",", "{", "}") {
//        val value = it.getter.call(target)
//        "${jsonString(it.name)}:${if(value is String) jsonString(value) else value}"
//    }.toString()
//}
//
//// 문자열이면 감싸는 따옴표 붙임
//// 문자열안에 따옴표는 \"로 변경
//private fun jsonString(v:String) = """"${v.replace("\"", "\\\"")}""""
//class Json0(val a:Int, val b:String )
//
//fun main() {
//    val json = Json0(10, "hello")
//    println(stringify(json))
//}