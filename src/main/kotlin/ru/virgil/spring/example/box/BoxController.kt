package ru.virgil.spring.example.box

import jakarta.annotation.security.RolesAllowed
import org.springframework.security.access.prepost.PostAuthorize
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ru.virgil.spring.example.truck.TruckService
import ru.virgil.spring.tools.security.cors.GlobalCors
import ru.virgil.spring.tools.util.Http.orNotFound
import java.util.*

@GlobalCors
@RestController
@RequestMapping("/box")
class BoxController(
    private val truckService: TruckService,
    private val boxService: BoxService,
    private val boxSecurity: BoxSecurity,
    private val boxGenerator: BoxGenerator,
) : BoxMapper {

    @GetMapping("/generate")
    fun generate(@RequestParam portions: Int = 10) =
        boxGenerator.generate(portions).let { boxGenerator.repository.saveAll(it) }
            .map { it.toDto() }

    @GetMapping
    fun getAll(@RequestParam page: Int, @RequestParam size: Int) = boxService.getAll(page, size)
        .map { it.toDto() }

    @RolesAllowed("POLICE")
    @GetMapping("/weapons")
    fun getAllWeapons() = boxService.getAllMyWeaponBoxes()
        .map { it.toDto() }

    @PostAuthorize(
        """
        hasRole('ROLE_POLICE') and @boxSecurity.hasWeapon(returnObject)
        or hasRole('ROLE_USER') and not @boxSecurity.hasWeapon(returnObject)
    """
    )
    @GetMapping("/{uuid}")
    fun get(@PathVariable uuid: UUID): BoxDto {
        val box = boxService.get(uuid).orNotFound()
        return box.toDto()
    }

    @PreAuthorize(
        """
        hasRole('ROLE_POLICE') and @boxSecurity.hasWeapon(#boxDto)
        or hasRole('ROLE_USER') and not @boxSecurity.hasWeapon(#boxDto)
    """
    )
    @PostMapping
    fun post(@RequestBody boxDto: BoxDto): BoxDto {
        // TODO: Заменить на Object Provider
        val assignedTruck = truckService.assignTruck().orNotFound()
        val createdBox = boxService.create(assignedTruck, boxDto)
        return createdBox.toDto()
    }

    @PutMapping("/{uuid}")
    fun put(@PathVariable uuid: UUID, @RequestBody boxDto: BoxDto): BoxDto {
        val serverBox = boxService.edit(uuid, boxDto)
        return serverBox.toDto()
    }

    @DeleteMapping("/{uuid}")
    fun delete(@PathVariable uuid: UUID) {
        boxService.delete(uuid)
    }
}
