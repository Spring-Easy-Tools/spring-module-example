package ru.virgil.spring.example.system

import org.instancio.Select
import org.instancio.TargetSelector
import kotlin.reflect.KProperty1
import kotlin.reflect.jvm.javaField

/**
 * Instancio selectors helper for Kotlin properties.
 *
 * Instancio's Select.field(...) method expects Java getter method references (e.g. Pojo::getValue).
 * Kotlin property references (e.g. Pojo::value) are not always resolvable via that mechanism,
 * therefore we resolve the backing Java field explicitly.
 */
object KSelect {

    fun <T : Any, V> field(property: KProperty1<T, V>): TargetSelector {
        val javaField = requireNotNull(property.javaField) {
            "Property '${property.name}' does not have a backing Java field"
        }
        return Select.field(javaField.declaringClass, javaField.name)
    }
}
