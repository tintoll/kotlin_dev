package day3_oop

fun read(msg: String, vararg answer: String, vali: ((String) -> Boolean)? = null): String {
    do {
        print(msg)
        val line = readLine()
        if (line != null && line.isNotBlank()) {
            if (answer.isEmpty() || answer.any { line == it }) {
                if (vali == null || vali(line)) {
                    return line
                }
            }
        }

    } while (true)
}

fun readN(title:String, msg:String, vararg vali:String, block:(String) -> Unit) {
    var itemMsg = msg
    var itemVali = vali
    do {
        println(title)
        val line = read(itemMsg, *itemVali)
        if (line == "0") break
        block(line)
        itemMsg = "0.noMore "+ itemMsg
        itemVali = itemVali.toMutableList().also { it += "0" }.toTypedArray()
    }while (true)
}