package ru.virgil.spring.example.truck

import org.springframework.web.bind.annotation.*
import ru.virgil.spring.example.box.BoxDto
import ru.virgil.spring.example.box.BoxMapper
import ru.virgil.spring.example.box.BoxService
import ru.virgil.spring.example.system.rest.RestValues
import ru.virgil.spring.tools.security.cors.GlobalCors
import ru.virgil.spring.tools.util.Http.orNotFound
import java.util.*

@GlobalCors
@RestController
@RequestMapping("/truck")
class TruckController(
    private val truckService: TruckService,
    override val boxService: BoxService,
) : BoxMapper, TruckMapper {

    @GetMapping("/uuid")
    fun getTruck(uuid: UUID): TruckDto {
        val truck = truckService.get(uuid).orNotFound()
        return truck.toDto()
    }

    @GetMapping("/{truckUuid}/box")
    fun getBoxesByTruck(
        @PathVariable truckUuid: UUID, @RequestParam(RestValues.PAGE) page: Int,
        @RequestParam(RestValues.SIZE) size: Int,
    ): List<BoxDto> {
        val truck = truckService.get(truckUuid).orNotFound()
        val boxes = boxService.getAll(truck, page, size)
        return boxes.map { it.toDto() }
    }
}
