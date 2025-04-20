package ru.virgil.spring.example.box

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import ru.virgil.spring.example.truck.Truck
import ru.virgil.spring.tools.security.Security.getSimpleCreator
import ru.virgil.spring.tools.util.Http.orNotFound
import java.util.*

@Service
class BoxService(
    private val boxRepository: BoxRepository,
) : BoxMapper {

    fun getAll(page: Int, size: Int) =
        boxRepository.findAllByCreatedByAndDeletedIsFalse(getSimpleCreator(), PageRequest.of(page, size))

    fun getAll(truck: Truck, page: Int, size: Int) =
        boxRepository.findAllByCreatedByAndTruckAndDeletedIsFalse(
            getSimpleCreator(),
            truck,
            PageRequest.of(page, size)
        )

    fun get(uuid: UUID) =
        boxRepository.findByCreatedByAndUuidAndDeletedIsFalse(getSimpleCreator(), uuid)

    fun create(truck: Truck, boxDto: BoxDto): Box {
        val box = boxDto.toEntity(truck)
        return boxRepository.save(box)
    }

    fun edit(uuid: UUID, patchBox: BoxDto): Box {
        var box = get(uuid).orNotFound()
        box = box merge patchBox
        return boxRepository.save(box)
    }

    fun delete(uuid: UUID): Box {
        val box = get(uuid).orNotFound()
        box.deleted = true
        return boxRepository.save(box)
    }

    fun getAllMyWeaponBoxes() =
        boxRepository.findAllByCreatedByAndTypeAndDeletedIsFalse(getSimpleCreator(), BoxType.WEAPON)

    fun countMy() = boxRepository.countAllByCreatedByAndDeletedIsFalse(getSimpleCreator())

    fun findBestBoxByTruck(truck: Truck): UUID? {
        val boxes = boxRepository.findAllByCreatedByAndTruckAndDeletedIsFalse(
            getSimpleCreator(), truck, PageRequest.of(0, 1)
        )
        return boxes.randomOrNull()?.uuid
    }
}
