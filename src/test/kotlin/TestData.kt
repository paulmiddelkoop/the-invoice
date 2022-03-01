package se.pamisoft.theinvoice

import se.pamisoft.theinvoice.family.*
import se.pamisoft.theinvoice.family.Delivery.E_INVOICE
import se.pamisoft.theinvoice.family.Delivery.POST
import se.pamisoft.theinvoice.family.income.Income
import java.time.LocalDate
import java.util.UUID

fun guardian(
    id: UUID = GUARDIAN_ID1,
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
    customerNumber: String? = CUSTOMER_NUMBER,
    endedOn: LocalDate? = null
) = Family(id, guardian1, guardian2, personalIdentityNumber, delivery, email, address, customerNumber, endedOn)

fun singleParentFamily(
    id: UUID = FAMILY_ID2,
    guardian: Guardian = guardian(),
    personalIdentityNumber: PersonalIdentityNumber = PERSONAL_IDENTITY_NUMBER,
    delivery: Delivery = E_INVOICE,
    email: String = "john@gmail.com",
    customerNumber: String? = CUSTOMER_NUMBER,
    endedOn: LocalDate? = null,
) = Family(id, guardian, null, personalIdentityNumber, delivery, email, null, customerNumber, endedOn)

fun maxIncome(
    id: UUID = INCOME_ID,
    changedOn: LocalDate = NOW
) = Income(id, true, null, changedOn)

fun income(
    amount: Int = 40_0000,
    id: UUID = INCOME_ID,
    changedOn: LocalDate = NOW
) = Income(id, false, amount, changedOn)

val FAMILY_ID1: UUID = UUID.randomUUID()
val FAMILY_ID2: UUID = UUID.randomUUID()
val GUARDIAN_ID1: UUID = UUID.randomUUID()
val GUARDIAN_ID2: UUID = UUID.randomUUID()
val INCOME_ID: UUID = UUID.randomUUID()
val PERSONAL_IDENTITY_NUMBER = PersonalIdentityNumber("19890201-3286")
const val CUSTOMER_NUMBER = "80"
val NOW: LocalDate = LocalDate.now()