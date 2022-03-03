package se.pamisoft.theinvoice.family

import java.time.LocalDate
import java.util.UUID

data class Child(
    val id: UUID = UUID.randomUUID(),
    val parents: Family,
    val firstName: String,
    val lastName: String,
    val bornOn: LocalDate
)