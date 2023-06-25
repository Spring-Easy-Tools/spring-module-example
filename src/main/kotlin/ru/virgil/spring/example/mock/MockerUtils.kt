package ru.virgil.spring.example.mock

import org.springframework.beans.factory.ObjectProvider
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.security.core.userdetails.UserDetails
import ru.virgil.spring.example.system.entity.OwnedRepository
import ru.virgil.spring.tools.util.data.Identified
import ru.virgil.spring.tools.util.data.Owned
import java.util.*

interface MockerUtils {

    fun <Entity> mock(
        number: Int,
        mocker: ObjectProvider<Entity>,
        repository: CrudRepository<Entity, UUID>,
    ) {
        val entities = ArrayList<Entity>()
        for (i in 1..number) entities.add(mocker.getObject())
        repository.saveAll(entities)
    }

    /**
     * При мокировании сначала может понадобиться найти какой-то определенный элемент. Например, грузовик без ящиков,
     * чтобы заполнить его ящиками. А если грузовиков без ящиков не осталось, то можно взять случайный грузовик.
     */
    fun <Entity : Owned> OwnedRepository<Entity>.findPriorityOrGetRandom(
        owner: UserDetails,
        priorityPredicate: (Entity) -> Boolean,
    ): Entity {
        val entities = this.findAllByCreatedBy(owner)
        val priorityEntities = entities.filter(priorityPredicate)
        return if (priorityEntities.isNotEmpty()) {
            priorityEntities.random()
        } else {
            entities.random()
        }
    }

    /**
     * При мокировании сначала может понадобиться найти какой-то определенный элемент. Например, грузовик без ящиков,
     * чтобы заполнить его ящиками. А если грузовиков без ящиков не осталось, то можно взять случайный грузовик.
     *
     */
    fun <Entity : Identified> JpaRepository<Entity, UUID>.findPriorityOrGetRandom(
        priorityPredicate: (Entity) -> Boolean,
    ): Entity {
        val entities = this.findAll()
        val priorityEntities = entities.filter(priorityPredicate)
        return if (priorityEntities.isNotEmpty()) {
            priorityEntities.random()
        } else {
            entities.random()
        }
    }
}
