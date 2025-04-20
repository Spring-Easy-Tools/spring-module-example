package ru.virgil.spring.example.stats

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import ru.virgil.spring.example.user.UserSettingsMapper
import ru.virgil.spring.example.user.UserSettingsService
import ru.virgil.spring.tools.security.Security

@Controller
@RequestMapping("/")
class StatsSiteController(
    private val statsService: StatsService,
    private val userSettingsService: UserSettingsService,
    private val yamlMapper: YAMLMapper,
    private val userDetailsService: UserDetailsManager,
) : UserSettingsMapper {

    @ModelAttribute("user_settings")
    fun getUserSettings(): String? {
        val userSettings = userSettingsService.get() ?: return null
        val userSettingsDto = userSettings.toDto()
        return yamlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userSettingsDto)
    }

    @ModelAttribute("anon")
    fun isAuthenticated() = Security.getAuthentication() is AnonymousAuthenticationToken

    @ModelAttribute("auth_type")
    fun getAuthType(): String = Security.getAuthentication().javaClass.simpleName

    @ModelAttribute("all_stats")
    fun getAllStats() = statsService.getAllStats().toString()

    @ModelAttribute("my_stats")
    fun getMyStats() = try {
        statsService.getMyStats().toString()
    } catch (e: SecurityException) {
        "Not authorized"
    }

    @GetMapping
    fun showStats() = "/index.html"
}
