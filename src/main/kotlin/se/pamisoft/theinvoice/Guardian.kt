package se.pamisoft.theinvoice

import java.util.UUID

data class Guardian(
    val id: UUID,
    val firstName: String,
    val lastName: String
) {
    val name = "$firstName $lastName"
}