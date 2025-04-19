package ru.virgil.spring.example.box

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import ru.virgil.spring.example.truck.Truck
import ru.virgil.spring.tools.security.Security.getSimpleCreator
import java.util.*

@Service
class BoxService(
    private val boxRepository: BoxRepository,
) : BoxMapper {

    fun getAll(page: Int, size: Int): List<Box> =
        boxRepository.findAllByCreatedByAndDeletedIsFalse(getSimpleCreator(), PageRequest.of(page, size))

    fun getAll(truck: Truck, page: Int, size: Int): List<Box> =
        boxRepository.findAllByCreatedByAndTruckAndDeletedIsFalse(
            getSimpleCreator(),
            truck,
            PageRequest.of(page, size)
        )

    fun get(uuid: UUID): Box =
        boxRepository.findByCreatedByAndUuidAndDeletedIsFalse(getSimpleCreator(), uuid).orElseThrow()

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
        boxRepository.findAllByCreatedByAndTypeAndDeletedIsFalse(getSimpleCreator(), BoxType.WEAPON)

    fun countMy(): Long = boxRepository.countAllByCreatedByAndDeletedIsFalse(getSimpleCreator())

    fun findBestBoxByTruck(truck: Truck): UUID? {
        val boxes = boxRepository.findAllByCreatedByAndTruckAndDeletedIsFalse(
            getSimpleCreator(), truck, PageRequest.of(0, 1)
        )
        return boxes.randomOrNull()?.uuid
    }
}
