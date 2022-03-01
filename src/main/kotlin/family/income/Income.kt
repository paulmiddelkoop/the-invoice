package se.pamisoft.theinvoice.family.income

import java.time.LocalDate
import java.util.UUID

data class Income(
    val id: UUID = UUID.randomUUID(),
    val max: Boolean,
    val amount: Int?,
    val startsOn: LocalDate
) {
    init {
        require(!(max && amount != null)) { "Amount should be null for max income." }
        require(max || (amount != null && amount > 0)) { "Either supply max or an positive amount." }
    }
}