package day2

import kotlin.reflect.KProperty

//fun <T:Any> stringify(target:T):String {
//    // 1단계
////    target::class.members.filter{it is KProperty<*>}.forEach{
////        // it 은 실제로 KCallable<*> 이다.
////        val prop = it as KProperty<*> // 캐스팅해줘야한다.
////    }
//    // 2단계 filterIsInstance 이용하면 바로 반환값이 KProperty<*> 이다.
//    val builder = StringBuilder()
//    builder.append("{")
//    target::class.members.filterIsInstance<KProperty<*>>().forEach{
//        builder.append(it.name,":")
//        val value = it.getter.call(target)
//        builder.append(value, ",")
//    }
//    builder.append("}")
//    return "$builder"
//}
//
//class Json0(val a:Int, val b:String )
//
//fun main() {
//    val json = Json0(10, "hello")
//    println(stringify(json))
//}