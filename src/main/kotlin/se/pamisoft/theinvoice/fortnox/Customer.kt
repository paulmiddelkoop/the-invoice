package se.pamisoft.theinvoice.fortnox

import se.pamisoft.theinvoice.fortnox.CustomerType.PRIVATE

data class Customer(
    val customerNumber: String,
    val name: String,
    val organisationNumber: String,
    val email: String?,
    val emailInvoice: String?,
    val defaultDeliveryTypes: DefaultDeliveryTypes,
    val type: CustomerType = PRIVATE,
    val address1: String?,
    val zipCode: String?,
    val city: String?,
    val active: Boolean
)

data class DefaultDeliveryTypes(
    val invoice: DefaultDeliveryType
)

@Suppress("SpellCheckingInspection")
enum class DefaultDeliveryType {
    PRINT,
    EMAIL,
    ELECTRONICINVOICE
}

enum class CustomerType {
    PRIVATE,
    COMPANY
}