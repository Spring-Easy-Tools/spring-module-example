package ru.virgil.spring.example.ping

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


// TODO: наследовать контроллер и применять корс в конце?
@CrossOrigin(
    origins = ["http://localhost:4200" /*"\${security.cors.origins}"*/ /*"\${security.cors.origins}", "#{  }"*/],
    allowCredentials = true.toString()
)
//@GlobalCors
//@CrossOrigin(
//    origins = ["\${security.cors.origins}"],
//    allowCredentials = true.toString()
//)
@RestController
@RequestMapping("/ping")
class PingController {

    @GetMapping
    fun pingAnonymous() = Unit

    @GetMapping("/auth")
    fun pingAuth() = Unit
}
