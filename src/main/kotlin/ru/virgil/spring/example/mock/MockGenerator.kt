package ru.virgil.spring.example.mock

import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import ru.virgil.spring.example.box.BoxMocker
import ru.virgil.spring.example.order.BuyingOrderMocker
import ru.virgil.spring.example.security.SecurityUser
import ru.virgil.spring.example.truck.TruckMocker
import ru.virgil.spring.example.user.UserSettingsMocker

private const val multiplier = 1

@Component
class MockGenerator(
    private val mockRecordRepository: MockRecordRepository,
    @Lazy
    private val buyingOrderMocker: BuyingOrderMocker,
    @Lazy
    private val boxMocker: BoxMocker,
    @Lazy
    private val truckMocker: TruckMocker,
    @Lazy
    private val userSettingsMocker: UserSettingsMocker,
) {

    fun start(principal: SecurityUser) {
        if (alreadyMocked(principal)) return
        preGenerate()
        postGenerate()
        markAsMocked(principal)
    }

    private fun markAsMocked(principal: SecurityUser) {
        // todo: во время мокирования createdBy выставляется вручную
        val mockRecord = MockRecord(principal)
        mockRecordRepository.save(mockRecord)
    }

    private fun preGenerate() {
        generate(1, userSettingsMocker)
        generate(10, truckMocker)
        generate(5, buyingOrderMocker)
        generate(10, boxMocker)
    }

    private fun postGenerate() {

    }

    private fun alreadyMocked(principal: SecurityUser): Boolean {
        val mockRecords = mockRecordRepository.findAllByCreatedBy(principal)
        return mockRecords.isNotEmpty()
    }

    fun <Entity> generate(
        number: Int,
        mocker: EntityMocker<Entity>,
    ) {
        val entities = ArrayList<Entity>()
        for (i in 1..number * multiplier) entities.add(mocker.new())
        mocker.repository.saveAll(entities)
    }
}
