package se.pamisoft.theinvoice.development

import se.pamisoft.theinvoice.*
import se.pamisoft.theinvoice.Delivery.E_INVOICE
import se.pamisoft.theinvoice.Delivery.POST
import java.time.LocalDate
import java.util.UUID

fun guardian(
    id: UUID= GUARDIAN_ID1,
    firstName: String = "John",
    lastName: String = "Doe",
) = Guardian(id, firstName, lastName)

fun address(
    address: String = "Main street 42",
    zipCode: String = "10014",
    city: String = "New York"
) = Address(address, zipCode, city)

fun family(
    id: UUID = FAMILY_ID1,
    guardian1: Guardian = guardian(),
    guardian2: Guardian = guardian(GUARDIAN_ID2, "Jane", "Doe"),
    personalIdentityNumber: PersonalIdentityNumber = PERSONAL_IDENTITY_NUMBER,
    delivery: Delivery = E_INVOICE,
    email: String = "john@gmail.com",
    address: Address? = address().takeIf { delivery == POST },
    externalReference: String? = null,
    endedOn: LocalDate? = null
) = Family(id, guardian1, guardian2 , personalIdentityNumber, delivery, email, address, externalReference, endedOn)

fun singleParentFamily(
    id: UUID = FAMILY_ID2,
    guardian: Guardian = guardian(),
    personalIdentityNumber: PersonalIdentityNumber = PERSONAL_IDENTITY_NUMBER,
    delivery: Delivery = E_INVOICE,
    email: String = "john@gmail.com",
    externalReference: String? = null,
    endedOn: LocalDate? = null,
) = Family(id, guardian, null, personalIdentityNumber, delivery, email, null, externalReference, endedOn)

val FAMILY_ID1: UUID = UUID.randomUUID()
val FAMILY_ID2: UUID = UUID.randomUUID()
val GUARDIAN_ID1: UUID = UUID.randomUUID()
val GUARDIAN_ID2: UUID = UUID.randomUUID()
val PERSONAL_IDENTITY_NUMBER = PersonalIdentityNumber("19890201-3286")
val NOW: LocalDate = LocalDate.now()