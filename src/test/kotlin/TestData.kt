package se.pamisoft.theinvoice

import se.pamisoft.theinvoice.family.*
import se.pamisoft.theinvoice.family.Delivery.E_INVOICE
import se.pamisoft.theinvoice.family.Delivery.POST
import se.pamisoft.theinvoice.family.child.Child
import se.pamisoft.theinvoice.family.income.Income
import java.time.LocalDate
import java.util.UUID
import java.util.UUID.randomUUID

fun guardian(
    name: String = "John Doe",
    id: UUID = randomUUID()
) = guardian(name.substringBefore(' '), name.substringAfter(' '), id)

fun guardian(
    firstName: String = "John",
    lastName: String = "Doe",
    id: UUID = randomUUID(),
) = Guardian(id, firstName, lastName)

fun address(
    address: String = "Main Street 42",
    zipCode: String = "10014",
    city: String = "New York"
) = Address(address, zipCode, city)

fun family(
    guardian1: Guardian = guardian("John Doe"),
    guardian2: Guardian = guardian("Jane Doe"),
    personalIdentityNumber: PersonalIdentityNumber = PERSONAL_IDENTITY_NUMBER,
    delivery: Delivery = E_INVOICE,
    email: String = "john@gmail.com",
    address: Address? = address().takeIf { delivery == POST },
    customerNumber: CustomerNumber = CUSTOMER_NUMBER,
    endedOn: LocalDate? = null,
    id: UUID = randomUUID()
) = Family(id, guardian1, guardian2, personalIdentityNumber, email, delivery, address, customerNumber, endedOn)

fun singleParentFamily(
    guardian: Guardian = guardian(),
    personalIdentityNumber: PersonalIdentityNumber = PERSONAL_IDENTITY_NUMBER,
    delivery: Delivery = E_INVOICE,
    email: String = "john@gmail.com",
    customerNumber: CustomerNumber = CUSTOMER_NUMBER,
    endedOn: LocalDate? = null,
    id: UUID = randomUUID()
) = Family(id, guardian, null, personalIdentityNumber, email, delivery, null, customerNumber, endedOn)

fun maxIncome(
    changedOn: LocalDate = NOW,
    id: UUID = randomUUID(),
) = Income(id, true, null, changedOn)

fun income(
    amount: Int = 40_0000,
    changedOn: LocalDate = NOW,
    id: UUID = INCOME_ID
) = Income(id, false, amount, changedOn)

fun child(
    firstName: String = "Molly",
    lastName: String = "Doe",
    bornOn: LocalDate = NOW.minusYears(3),
    parents: Family = family(),
    id: UUID = randomUUID(),
) = Child(id, parents, firstName, lastName, bornOn)

fun familyDto(
    id: UUID = randomUUID(),
    guardian1: Guardian = guardian("John Doe"),
    guardian2: Guardian = guardian("Jane Doe"),
    personalIdentityNumber: PersonalIdentityNumber = PersonalIdentityNumber("19890201-3286"),
    email: String = "john@gmail.com",
    delivery: Delivery = POST,
    address: Address = address("Main Street 42", "10014", "New York"),
    customerNumber: CustomerNumber? = null
) = FamilyDto(
    id,
    guardian1,
    guardian2,
    personalIdentityNumber,
    email,
    delivery,
    address,
    customerNumber
)

val FAMILY_ID1: UUID = randomUUID()
val FAMILY_ID2: UUID = randomUUID()
val GUARDIAN_ID1: UUID = randomUUID()
val GUARDIAN_ID2: UUID = randomUUID()
val CHILD_ID: UUID = randomUUID()
val INCOME_ID: UUID = randomUUID()
val PERSONAL_IDENTITY_NUMBER = PersonalIdentityNumber("19890201-3286")
val CUSTOMER_NUMBER = CustomerNumber("80")
val NOW: LocalDate = LocalDate.now()