package ru.virgil.spring.example.security.v2

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SecurityUserV2Repository : JpaRepository<SecurityUserV2, String>
