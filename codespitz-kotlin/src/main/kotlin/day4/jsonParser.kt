package day4

import day2.Name
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KType
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.createType
import kotlin.reflect.full.findAnnotation

fun <T:Any> parseJon(target: T, json: String): T? {
    val lexer = JsonLexer(json)
    lexer.skipWhite()
    return parseObject(lexer, target)
}
fun <T:Any> T.fromJson(json: String): T? = parseJon(this, json)

class JsonLexer(val json: String) {
    val last = json.lastIndex
    var cursor = 0
        private set
    var curr = ' '
        get() = json[cursor]

    fun next() {
        if (cursor < last) ++cursor
    }

    fun skipWhite() {
        while ("\t\n\r".indexOf(curr) != -1 && cursor < last) next()
    }

    fun isOpenObject(): Boolean = '{' == curr
    fun isCloseObject(): Boolean = '}' == curr
    fun isComma() = curr == ','
    fun key(): String? {
        val result = string() ?: return null
        skipWhite()
        if (curr != ':') return null
        next()
        skipWhite()
        return result
    }

    fun string(): String? {
        if (curr != '"') return null
        next()
        val start = cursor
        var isSkip = false
        while (isSkip || curr != '"') {
            isSkip = if (isSkip) false else curr == '\\'
            next()
        }
        val result = json.substring(start, cursor)
        next()
        return result
    }

    fun number(): String? {
        var start = cursor
        while ("-.0123456789".indexOf(curr) != -1) next()
        return if (start == cursor) null else json.substring(start, cursor)
    }

    fun int() = number()?.toInt()
    fun long() = number()?.toLong()
    fun float() = number()?.toFloat()
    fun double() = number()?.toDouble()
    fun boolean(): Boolean? {
        return when {
            json.substring(cursor, cursor + 4) == "true" -> {
                cursor += 4
                true
            }

            json.substring(cursor, cursor + 5) == "false" -> {
                cursor += 5
                false
            }

            else -> null
        }
    }

    fun isOpenArray() = curr == '['
    fun isCloseArray() = curr == ']'
}

fun <T : Any> parseObject(lexer: JsonLexer, target: T): T? {
    if (!lexer.isOpenObject()) return null
    lexer.next()
    val props: Map<String, KMutableProperty<*>> = target::class.members.filterIsInstance<KMutableProperty<*>>()
        .associate {
            (it.findAnnotation<Name>()?.name ?: it.name) to it
        }
    while (!lexer.isCloseObject()) {
        lexer.skipWhite()
        val key = lexer.key() ?: return null
        val prop = props[key] ?: return null
        val value = jsonValue(lexer, prop.returnType) ?: return null
        prop.setter.call(target, value)
        lexer.skipWhite()
        if (lexer.isComma()) lexer.next()
    }
    lexer.next()
    return target
}

fun jsonValue(lexer: JsonLexer, type: KType): Any? {
    return when (val cls = type.classifier as? KClass<*> ?: return null) {
        String::class -> lexer.string()
        Int::class -> lexer.int()
        Long::class -> lexer.long()
        Float::class -> lexer.float()
        Double::class -> lexer.double()
        Boolean::class -> lexer.boolean()
        List::class -> parseList(lexer, type.arguments[0].type?.classifier as? KClass<*> ?: return null)
        else -> parseObject(lexer, cls.createInstance())
    }
}

fun parseList(lexer: JsonLexer, cls: KClass<*>): List<*>? {
    if (!lexer.isOpenArray()) return null
    lexer.next()
    val result = mutableListOf<Any>()
    val cls2 = cls.createType()
    while (!lexer.isCloseArray()) {
        lexer.skipWhite()
        val v = jsonValue(lexer, cls2) ?: return null
        result += v
        lexer.skipWhite()
        if (lexer.isComma()) lexer.next()
    }
    lexer.next()
    return result
}