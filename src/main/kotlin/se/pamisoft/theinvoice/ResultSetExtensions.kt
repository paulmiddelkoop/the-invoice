package se.pamisoft.theinvoice

import java.sql.ResultSet
import java.util.UUID

fun ResultSet.getUuid(column: String): UUID? = getObject(column, UUID::class.java)

fun ResultSet.getRequiredUuid(column: String): UUID = requireNotNull(getUuid(column)) {"$column was null."}