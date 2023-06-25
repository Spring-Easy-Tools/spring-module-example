package ru.virgil.spring.example.box

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import ru.virgil.spring.example.truck.Truck
import ru.virgil.spring.tools.security.oauth.SecurityUserFunctions.getPrincipal
import java.util.*


@Service
class BoxService(
    private val boxRepository: BoxRepository,
) : BoxMapper {

    fun getAll(page: Int, size: Int): List<Box> =
        boxRepository.findAllByCreatedByAndDeletedIsFalse(getPrincipal(), PageRequest.of(page, size))

    fun getAll(truck: Truck, page: Int, size: Int): List<Box> =
        boxRepository.findAllByCreatedByAndTruckAndDeletedIsFalse(getPrincipal(), truck, PageRequest.of(page, size))

    fun get(uuid: UUID): Box =
        boxRepository.findByCreatedByAndUuidAndDeletedIsFalse(getPrincipal(), uuid).orElseThrow()

    fun create(truck: Truck, boxDto: BoxDto): Box {
        val box = boxDto.toEntity(truck)
        return boxRepository.save(box)
    }

    fun edit(uuid: UUID, patchBox: BoxDto): Box {
        var box = get(uuid)
        box = box merge patchBox
        return boxRepository.save(box)
    }

    fun delete(uuid: UUID) {
        val box = get(uuid)
        box.deleted = true
        boxRepository.save(box)
    }

    fun getAllMyWeapons(): List<Box> =
        boxRepository.findAllByCreatedByAndTypeAndDeletedIsFalse(getPrincipal(), BoxType.WEAPON)

    fun countMy(): Long = boxRepository.countAllByCreatedByAndDeletedIsFalse(getPrincipal())
}
