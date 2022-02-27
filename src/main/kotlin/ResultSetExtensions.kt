package se.pamisoft.theinvoice

import java.sql.ResultSet
import java.time.LocalDate
import java.util.UUID

fun ResultSet.getUuid(column: String): UUID? = getObject(column, UUID::class.java)

fun ResultSet.getRequiredUuid(column: String): UUID = getNotNull(column) { getUuid(column) }

fun ResultSet.getLocalDate(column: String): LocalDate? = getObject(column, LocalDate::class.java)

fun ResultSet.getRequiredLocalDate(column: String): LocalDate = getNotNull(column) { getLocalDate(column) }

private fun <T> getNotNull(column: String, valueProvider: () -> T?) =
    requireNotNull(valueProvider()) { "$column was null." }