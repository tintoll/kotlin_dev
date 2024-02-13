package async

import java.util.*

class Coffee(val name: String)

// Subject
class Barista: Observable() {

    private lateinit var coffeeName: String

    fun orderCoffee(name: String) {
        coffeeName = name
    }

    fun makeCoffee() {
        setChanged()
        notifyObservers(Coffee(coffeeName))
    }
}

// Observer
class Customer(val name: String): Observer {
    override fun update(o: Observable?, arg: Any?) {
        val coffee = arg as Coffee
        println("${name}이 ${coffee.name}을 받았습니다.")
    }
}

fun main() {
    val barista = Barista()
    barista.orderCoffee("아메리카노")

    val customer1 = Customer("고객1")
    val customer2 = Customer("고객2")
    barista.addObserver(customer1)
    barista.addObserver(customer2)

    barista.makeCoffee()
}