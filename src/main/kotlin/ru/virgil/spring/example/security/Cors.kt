package ru.virgil.spring.example.security

import org.springframework.web.bind.annotation.CrossOrigin

@CrossOrigin(
    origins = ["http://localhost:4200/"],
    allowCredentials = true.toString()
)
annotation class Cors
