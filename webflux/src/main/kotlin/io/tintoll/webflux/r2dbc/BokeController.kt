package io.tintoll.webflux.r2dbc

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class BokeController(val bokeRespository: BokeRespository) {

    @GetMapping("/bokes/{name}")
    fun getByName(@PathVariable name: String): Mono<Boke> {
        return bokeRespository.findByName(name)
    }

    @PostMapping("/bokes")
    fun create(@RequestBody map: Map<String, Any>): Mono<Boke> {
        val boke = Boke(name = map["name"].toString(), price = map["price"] as Int)
        return bokeRespository.save(boke)
    }

}