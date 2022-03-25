package se.pamisoft.theinvoice.family.child

import com.fasterxml.jackson.annotation.JsonIgnore
import se.pamisoft.theinvoice.family.Family
import java.time.LocalDate
import java.util.UUID

data class Child(
    val id: UUID = UUID.randomUUID(),
    @JsonIgnore
    val parents: Family,
    val firstName: String,
    val lastName: String,
    val bornOn: LocalDate
) {
    val name = "$firstName $lastName"
}