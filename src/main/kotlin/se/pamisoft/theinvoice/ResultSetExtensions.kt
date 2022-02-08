package se.pamisoft.theinvoice

import java.sql.ResultSet
import java.util.*

fun ResultSet.getUUID(column: String): UUID? = getObject(column, UUID::class.java)