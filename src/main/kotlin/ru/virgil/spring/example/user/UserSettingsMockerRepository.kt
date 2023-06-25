package ru.virgil.spring.example.user

import org.springframework.stereotype.Repository
import ru.virgil.spring.example.mock.ByOwner
import ru.virgil.spring.example.mock.MockerRepository
import java.util.*

@Repository
interface UserSettingsMockerRepository : MockerRepository<UserSettings, UUID>, ByOwner<UserSettings>
