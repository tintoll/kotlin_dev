package day5

class Tree<T>(val value: T)
fun main() {
//    var tree: Tree<Number> = Tree<Int>(10) // error
    var tree: Tree<out Number> = Tree<Int>(10) // success
    tree.value.toDouble()

//    var mutableList: MutableList<Number> = mutableListOf<Int>(10) // Type mismatch: inferred type is MutableList<Int> but MutableList<Number> was expected
    var mutableList: MutableList<out Number> = mutableListOf<Int>(10)
    //mutableList.add(10.0)  // java는 이런식으로 한 이상 값이 들어올 수 있음. 하지만 Kotlin 에서는 안됨.
    println(mutableList)

    // 코틀린의 List Collection 은 기본적으로 immutable 하다.
    // 따라서 Producer 의 역할만 하게된다. 그래서 공변이 성립할 수 있게 되는 것이다.
    // 즉, Producer 역할만 하게 되는 경우에는 Covariance 하다.
    var list: List<Number> = listOf<Int>(10)
    println(list)

    val list2: List<out Number> = listOf<Int>(10)
}