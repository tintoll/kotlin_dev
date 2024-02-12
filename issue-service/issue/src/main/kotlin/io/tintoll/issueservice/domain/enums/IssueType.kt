package io.tintoll.issueservice.domain.enums

enum class IssueType {
    BUG, TASK;

    companion object {
        operator fun invoke(type: String) = valueOf(type.uppercase())
    }
}

//
//fun main() {
//    val type = IssueType("BUG")
//    print(IssueType.BUG == type)
//}