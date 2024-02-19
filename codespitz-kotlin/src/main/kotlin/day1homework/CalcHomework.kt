package day1homework

val number = """([+-]?[.\d]+)"""
var groupMD = """$number([*/])$number""".toRegex()
val plus = number.toRegex()
val trim = """[^.\d-+*/]""".toRegex()
val bracket = """\(([^)]+)\)""".toRegex()
fun reduceBracket(v:String):String {
    var str = v.replace(trim,"")
    while(bracket.containsMatchIn(str)) str = str.replace(bracket) {"${newCalc(it.groupValues[1])}"}
    return str
}

fun newCalc(v:String):Double {
    var str = reduceBracket(v)
    while(groupMD.containsMatchIn(str)) {
        str = str.replace(groupMD) {
            val (_, left, op, right) = it.groupValues
            val leftValue = left.toDouble()
            val rightValue = right.toDouble()
            "${when(op) {
                "*" -> leftValue * rightValue
                "/" -> leftValue / rightValue
                else -> throw Throwable("invalid operator $op")
            }}"
        }
    }
    return plus.findAll(str).fold(0.0) {acc, curr -> acc + curr.groupValues[1].toDouble()}
}


fun main() {
    println(newCalc("-2 * (-3 + 0.4) / -0.2"))
}
