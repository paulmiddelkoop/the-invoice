package se.pamisoft.theinvoice

import java.sql.ResultSet
import java.time.LocalDate
import java.util.UUID

fun ResultSet.getOptionalInt(column: String): Int? = getInt(column).takeUnless{ wasNull() }

fun ResultSet.getOptionalUuid(column: String): UUID? = getObject(column, UUID::class.java)

fun ResultSet.getRequiredUuid(column: String): UUID = getNotNull(column) { getOptionalUuid(column) }

fun ResultSet.getOptionalLocalDate(column: String): LocalDate? = getObject(column, LocalDate::class.java)

fun ResultSet.getRequiredLocalDate(column: String): LocalDate = getNotNull(column) { getOptionalLocalDate(column) }

private fun <T> getNotNull(column: String, valueProvider: () -> T?) =
    requireNotNull(valueProvider()) { "$column was null." }