package se.pamisoft.theinvoice

import java.time.LocalDate
import java.util.UUID

data class Income(
    val id: UUID = UUID.randomUUID(),
    val max: Boolean,
    val amount: Int?,
    val changedOn: LocalDate
) {
    init {
        require(max || (amount != null && amount > 0)) { "Require max of positive amount." }
    }
}