package se.pamisoft.theinvoice.family

import com.fasterxml.jackson.annotation.JsonIgnore
import dev.personnummer.Personnummer
import se.pamisoft.theinvoice.family.Delivery.POST
import java.time.LocalDate
import java.util.UUID

data class Family(
    val id: UUID = UUID.randomUUID(),
    val guardian1: Guardian,
    val guardian2: Guardian?,
    val personalIdentityNumber: PersonalIdentityNumber,
    val email: String,
    val delivery: Delivery,
    val address: Address? = null,
    val customerNumber: CustomerNumber,
    val endedOn: LocalDate? = null
) {
    @JsonIgnore
    val guardians = listOfNotNull(guardian1, guardian2)
    @Suppress("unused")
    val singleParent = guardian2 == null
    val name = familyName(guardian1, guardian2)

    init {
        require(delivery != POST || address != null) { "Address is required when using post delivery." }
    }
}

fun familyName(guardian1: Guardian, guardian2: Guardian?) =
    guardian2?.let {
        "${if (guardian1.lastName == guardian2.lastName) guardian1.firstName else guardian1.name} & ${guardian2.name}"
    } ?: guardian1.name

@JvmInline
value class CustomerNumber(val value: String)

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