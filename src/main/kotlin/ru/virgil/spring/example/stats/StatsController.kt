package ru.virgil.spring.example.stats

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.virgil.spring.tools.security.cors.GlobalCors

@Controller
@RequestMapping("/")
class StatsHtmlController(
    private val statsService: StatsService,
) {

    @ModelAttribute("all_stats")
    fun getAllStats() = statsService.getAllStats().toString()

    @ModelAttribute("my_stats")
    fun getMyStats() = try {
        statsService.getMyStats().toString()
    } catch (e: SecurityException) {
        "Not authorized"
    }

    @GetMapping
    fun showStats(): String = "index.html"

    @GetMapping("/login")
    fun loginPage() = "login.html"
}

@GlobalCors
@RestController
@RequestMapping("/stats")
class StatsController(
    private val statsService: StatsService,
) {

    @GetMapping("/all")
    fun getAllStats() = statsService.getAllStats()

    @GetMapping("/my")
    fun getMyStats() = statsService.getMyStats()
}
