package ru.virgil.spring.example.system

import jakarta.persistence.GeneratedValue
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.instancio.Instancio
import org.instancio.Model
import org.instancio.Select
import org.instancio.settings.Keys
import org.instancio.settings.Settings
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import ru.virgil.spring.tools.entity.Soft

@Component
class InstancioProvider {

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

    fun <T> createModel(entityClass: Class<T>): Model<T> {
        return Instancio.of(entityClass)
            .lenient()
            .ignore(Select.fields().named("uuid"))
            .ignore(Select.all(CreatedBy::class.java))
            .ignore(Select.all(CreationTimestamp::class.java))
            .ignore(Select.all(UpdateTimestamp::class.java))
            .ignore(Select.all(GeneratedValue::class.java))
            .set(Select.field(Soft::deleted.name), false)
            .withSettings(Settings.create().set(Keys.COLLECTION_MIN_SIZE, 1).set(Keys.COLLECTION_MAX_SIZE, 10))
            .toModel()
    }
}
