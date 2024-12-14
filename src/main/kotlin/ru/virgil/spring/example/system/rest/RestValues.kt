package ru.virgil.spring.example.system.rest

/**
 * Можно выделять постоянно повторяемые в API параметры в отдельный объект.
 * Это защитит от расхождения в названиях, типа pages и page.
 * И позволит не повторять названия параметров в каждом контроллере.
 */
object RestValues {

    const val page = "page"
    const val size = "size"
}
