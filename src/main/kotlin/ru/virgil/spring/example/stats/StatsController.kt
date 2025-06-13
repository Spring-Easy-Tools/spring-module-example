package ru.virgil.spring.example.stats

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.virgil.spring.tools.security.cors.GlobalCors

@GlobalCors
@RestController
@RequestMapping("/stats")
class StatsController(
    private val statsService: StatsService,
) {

    @GetMapping("/generate")
    fun generate() {
        throw NotImplementedError("Генерация всей статистики из одного места пока не реализована")
    }

    @GetMapping("/all")
    fun getAllStats() = statsService.getAllStats()

    @GetMapping("/my")
    fun getMyStats() = statsService.getMyStats()
}
