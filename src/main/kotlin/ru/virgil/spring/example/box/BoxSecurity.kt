package ru.virgil.spring.example.box

import org.springframework.stereotype.Component

// TODO: Можно превратить в объект?
// todo: какой из методов используется?
@Component
class BoxSecurity {

    fun hasWeapon(boxDto: BoxDto): Boolean = boxDto.type!! == BoxType.WEAPON

    companion object {

        fun hasWeapon(boxDto: BoxDto): Boolean = boxDto.type!! == BoxType.WEAPON
    }
}
