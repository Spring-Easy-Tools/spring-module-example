package ru.virgil.spring.example.system

import jakarta.persistence.GeneratedValue
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import org.jeasy.random.FieldPredicates
import org.springframework.context.annotation.Bean
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import ru.virgil.spring.example.box.BoxGenerator
import ru.virgil.spring.example.order.BuyingOrderGenerator
import ru.virgil.spring.example.truck.TruckGenerator
import ru.virgil.spring.tools.util.data.Soft

@Component
class EasyRandomProvider {

    interface Generator<Data : Any> {

        val repository: CrudRepository<Data, *>

        fun generate(): Data

        fun generateAndSave(): Data {
            return generate().also { repository.save(it) }
        }

        fun generate(count: Int): List<Data>

        fun generateAndSave(count: Int): List<Data> {
            return generate(count).also { repository.saveAll(it) }
        }
    }

    interface Parameters {

        fun apply(parameters: EasyRandomParameters): EasyRandomParameters = parameters
    }

    private fun EasyRandomParameters.applyBasicParameters(): EasyRandomParameters {
        return this.excludeField(
            FieldPredicates.isAnnotatedWith(
                CreatedBy::class.java,
                CreationTimestamp::class.java,
                UpdateTimestamp::class.java,
                GeneratedValue::class.java
            )
        )
            .randomize(FieldPredicates.named(Soft::deleted.name)) { false }
            .collectionSizeRange(1, 10)
    }

    @Bean
    fun provideEasyRandom(): EasyRandom {
        val randomParameters = EasyRandomParameters()
            .applyBasicParameters()
            .also { BuyingOrderGenerator.apply(it) }
            .also { TruckGenerator.apply(it) }
            .also { BoxGenerator.apply(it) }
        return EasyRandom(randomParameters)
    }
}
