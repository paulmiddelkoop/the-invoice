package se.pamisoft.theinvoice

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.UUID

data class Guardian(
    val id: UUID,
    val firstName: String,
    val lastName: String
) {
    @JsonIgnore
    val name = "$firstName $lastName"
}