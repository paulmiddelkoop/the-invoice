package se.pamisoft.theinvoice.family

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.UUID

data class Guardian(
    val id: UUID = UUID.randomUUID(),
    val firstName: String,
    val lastName: String
) {
    @JsonIgnore
    val name = "$firstName $lastName"
}