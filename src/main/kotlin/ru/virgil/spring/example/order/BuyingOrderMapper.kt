package ru.virgil.spring.example.order

interface BuyingOrderMapper {

    fun BuyingOrder.toDto() = BuyingOrderDto(
        createdAt = createdAt,
        updatedAt = updatedAt,
        uuid = uuid,
        description = description,
    )

    infix fun BuyingOrder.merge(buyingOrderDto: BuyingOrderDto): BuyingOrder {
        description = buyingOrderDto.description ?: description
        return this
    }
}
