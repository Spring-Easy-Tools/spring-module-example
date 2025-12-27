package ru.virgil.spring.example.ping

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/ping")
class PingController {

    @GetMapping
    fun pingAnonymous() = Unit

    @GetMapping("/auth")
    fun pingAuth() = Unit
}
