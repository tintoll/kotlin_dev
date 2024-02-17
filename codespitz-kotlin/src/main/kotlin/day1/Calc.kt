package day1

// '' : char literal
// "" : string literal
// """ : no escaping, newlines

// 해당 안에 있는 문자 이외의 글자를 찾는 정규 표현식
val trim = """[^.\d-+*/]""".toRegex()
// [...] : Character class
// [^...] : Exception character class
// \d : 0,1,2,3,4,5,6,7,8,9

fun trim(v: String): String {
    return v.replace(trim, "")
}
// -를 -+로 교체
// 코틀린에서는 replace가 다른 언어의 replaceAll과 같은 기능을 한다.
fun repMtoPM(v:String) = v.replace("-","+-")

var groupMD = """((?:\+|\+-)?[.\d]+)([*/])((?:\+|\+-)?[.\d]+)""".toRegex()
// (...) : capturing group
// (?:...) : non-capturing group
// (..|..) : alternative
// ? : 0 or 1
// + : 1 or more
fun foldGroup(v:String):Double = groupMD.findAll(v).fold(0.0){acc, curr ->
    val (_, left, op, right) = curr.groupValues
    val leftValue = left.replace("+","").toDouble()
    val rightValue = right.replace("+","").toDouble()
    val result = when(op) {
        "*" -> leftValue * rightValue
        "/" -> leftValue / rightValue
        else -> throw Throwable("invalid operator $op")
    }
    acc + result
}

fun calc(v:String) = foldGroup(repMtoPM(trim(v)))

fun main() {
    println(calc("-2 * -3 + 0.4 / -0.2"))
}