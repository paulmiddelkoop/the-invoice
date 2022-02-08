package se.pamisoft.theinvoice

import se.pamisoft.theinvoice.Delivery.POST
import java.time.LocalDate
import java.util.UUID

data class Family(
    val id: UUID,
    val guardian1: Guardian,
    val guardian2: Guardian?,
    val personalIdentityNumber: PersonalIdentityNumber,
    val delivery: Delivery,
    val email: String,
    val address: Address? = null,
    val externalReference: String? = null,
    val endedOn: LocalDate? = null
) {
    val guardians = listOfNotNull(guardian1, guardian2)

    val name = guardian2?.let {
        "${if (guardian1.lastName == guardian2.lastName) guardian1.firstName else guardian1.name} & ${guardian2.name}"
    } ?: guardian1.name

    init {
        require(delivery != POST || address != null) { "Address is required when using post delivery. " }
    }
}

// TODO: complete
@JvmInline
value class PersonalIdentityNumber(val value: String)

data class Address(
    val address: String,
    val zipCode: String,
    val city: String
)

enum class Delivery(val value: String) {
    E_INVOICE("e-invoice"), EMAIL("email"), POST("post");

    companion object {
        fun of(value: String) =
            enumValues<Delivery>().firstOrNull { it.value == value }
                ?: throw IllegalArgumentException("Unknown delivery: '$value'.")
    }
}