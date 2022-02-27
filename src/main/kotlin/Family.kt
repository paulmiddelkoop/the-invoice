package se.pamisoft.theinvoice

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import dev.personnummer.Personnummer
import se.pamisoft.theinvoice.Delivery.POST
import java.time.LocalDate
import java.util.UUID

@JsonPropertyOrder(
    "id",
    "name",
    "guardian1",
    "guardian2",
    "personalIdentityNumber",
    "delivery",
    "email",
    "address",
    "customerNumber",
    "endedOn"
)
data class Family(
    val id: UUID = UUID.randomUUID(),
    val guardian1: Guardian,
    val guardian2: Guardian?,
    val personalIdentityNumber: PersonalIdentityNumber,
    val delivery: Delivery,
    val email: String,
    val address: Address? = null,
    val customerNumber: String? = null,
    val endedOn: LocalDate? = null,
    val incomes: List<Income> = emptyList()
) {
    @JsonIgnore
    val guardians = listOfNotNull(guardian1, guardian2)
    val singleParent = guardian2 == null

    val name = guardian2?.let {
        "${if (guardian1.lastName == guardian2.lastName) guardian1.firstName else guardian1.name} & ${guardian2.name}"
    } ?: guardian1.name

    init {
        require(delivery != POST || address != null) { "Address is required when using post delivery. " }
    }
}

@JvmInline
value class PersonalIdentityNumber private constructor(val value: String) {
    companion object {
        operator fun invoke(value: String): PersonalIdentityNumber =
            PersonalIdentityNumber(Personnummer.parse(value).run {
                "$fullYear$month$day${separator()}$numbers"
            })
    }
}

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