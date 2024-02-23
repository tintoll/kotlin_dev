package day5

interface Validator {
    // Result는 코틀린이 제공하는 대수타입이다.
    fun <T : Any> check(v: Any): Result<T>
}

class RuleValidator(block: RuleDSL.() -> Unit) : Validator {
    private val ruleDSL: RuleDSL by lazy { RuleDSL(block) }
    override fun <T : Any> check(v: Any): Result<T> {
        return when (val result = ruleDSL.check(v)) {
            is RuleResult.Value<*> -> (result.value as? T)?.let { Result.success(it) }
                ?: Result.failure(Throwable(("invalid type : ${result.value}")))

            is RuleResult.Fail -> Result.failure(Throwable(result.msg))
        }
    }
}


fun main() {
    val vali = RuleValidator {
        Case {
            trim()
            length(5, "길이가 5가 아닙니다.")
        }
    }
    vali.check<String>("abcde")
        .onSuccess { println("ok $it") }
        .onFailure { println("fail ${it.message}")}
}