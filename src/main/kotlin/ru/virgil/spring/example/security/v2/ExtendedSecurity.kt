package ru.virgil.spring.example.security.v2

import ru.virgil.spring.tools.security.Security.getUserDetailsCreator

object ExtendedSecurity {

    fun getSecurityUserCreator() = getUserDetailsCreator() as SecurityUserV2
}
