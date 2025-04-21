package ru.virgil.spring.example.box

import org.springframework.stereotype.Component

@Component
class BoxSecurity {

    /**
     * Используется в языке безопасности
     * */
    @Suppress("unused")
    fun hasWeapon(boxDto: BoxDto): Boolean = boxDto.type == BoxType.WEAPON
}
