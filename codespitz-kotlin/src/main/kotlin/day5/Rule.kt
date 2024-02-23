package day5

interface Rule {
    fun check(target: RuleResult): RuleResult
}

sealed interface RuleResult {
    companion object {
        private val defaultMsg = "invalid"
        fun <T : Any> value(v: T): RuleResult = Value(v)
        fun fail(msg: String?): RuleResult = Fail(msg ?: defaultMsg)
    }

    data class Value<T : Any>(val value: T) : RuleResult
    data class Fail(val msg: String) : RuleResult
}

class RuleDSL(block: RuleDSL.() -> Unit) {

    private val cases = mutableSetOf<AddRules>()
    fun Case(block: AddRules.() -> Unit) {
        cases += AddRules(block)
    }

    fun check(v: Any): RuleResult {
        var result = RuleResult.value(v)
        cases.any {
            result = RuleResult.value(v)
            it.all { rule ->
                result = rule.check(result)
                when (result) {
                    is RuleResult.Value<*> -> true
                    is RuleResult.Fail -> false
                }
            }
        }
        return result
    }

    init {
        block()
    }
}

// 인터페이스 델리게이션
// 인터페이스 선언시 by를 붙여 인스턴스를 넘기면 인터페이시의 메소드를 모두 해당 인스턴스가 처리하게 자동으로 코드를 생성한다.
class AddRules(block: AddRules.() -> Unit) : MutableSet<Rule> by mutableSetOf() {
    // 원래대로라면 MutableSet의 메소드를 직접 구현해야 하지만, by를 사용하면 자동으로 구현된다.
    init {
        block(this)
    }

    inner class equal(private val base: Any, private val msg: String?) : Rule {
        override fun check(target: RuleResult): RuleResult {
            return if (target is RuleResult.Value<*> && target.value == base) {
                target
            } else {
                RuleResult.fail(msg)
            }
        }
    }
}

//AddRule {
//    equal(1, "1이 아닙니다.")
//}
// 또다른 length 를 추가해야한다. 이때 확장함수를 이용한다.
class Length(private val length: Int, private val msg: String?) : Rule {
    override fun check(target: RuleResult): RuleResult {
        return if (target is RuleResult.Value<*>
            && target.value is String
            && target.value.length == length
        ) {
            target
        } else {
            RuleResult.fail(msg)
        }
    }
}

fun AddRules.length(length: Int, msg: String?) {
    this += Length(length, msg)
}

class Trim(private val msg: String?) : Rule {
    override fun check(target: RuleResult): RuleResult {
        return if (target is RuleResult.Value<*>
            && target.value is String
        ) RuleResult.value(target.value.trim()) else RuleResult.fail(msg)
    }
}
fun AddRules.trim(msg: String? = null) {
    this += Trim(msg)
}

