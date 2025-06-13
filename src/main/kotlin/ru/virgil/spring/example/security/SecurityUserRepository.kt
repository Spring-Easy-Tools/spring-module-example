package ru.virgil.spring.example.security

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SecurityUserRepository : JpaRepository<SecurityUser, String>
