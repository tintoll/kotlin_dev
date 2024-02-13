package async

data class Car(val brand:String)

class CarIterable(val cars: List<Car> = listOf()) : Iterable<Car> {
    override fun iterator(): Iterator<Car> = CarIterator(cars)
}

class CarIterator(val cars: List<Car>, var index: Int = 0) : Iterator<Car> {
    override fun hasNext(): Boolean = index < cars.size
    override fun next(): Car = cars[index++]
}

fun main() {
    val carIterable = CarIterable(listOf(Car("BMW"), Car("Benz"), Car("Audi")))
    val iterator = carIterable.iterator()
    while (iterator.hasNext()) {
        println(iterator.next())
    }
}