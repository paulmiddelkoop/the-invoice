package se.pamisoft.theinvoice.family.income

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import se.pamisoft.theinvoice.getOptionalInt
import se.pamisoft.theinvoice.getRequiredLocalDate
import se.pamisoft.theinvoice.getRequiredUuid
import java.sql.ResultSet
import java.util.UUID

@Repository
class IncomeRepository(private val db: NamedParameterJdbcTemplate) {
    fun upsert(income: Income, familyId: UUID) =
        db.update(
            """
            |insert into income (id, family_id, max, amount, starts_on)
            |values (:id, :family_id, :max, :amount, :starts_on)
            |on conflict (id) do update 
            |set max = excluded.max, 
            |    amount = excluded.amount,
            |    starts_on = excluded.starts_on""".trimMargin(),
            income.toMap(familyId)
        )

    fun findFor(familyId: UUID): List<Income> =
        db.query(
            "select * from income where family_id = :family_id order by starts_on desc",
            mapOf("family_id" to familyId)
        ) { rs, _ -> rs.getIncome() }

    fun delete(id: UUID) {
        db.update("delete from income where id = :id", mapOf("id" to id))
    }
}

private fun ResultSet.getIncome() =
    Income(
        id = getRequiredUuid("id"),
        max = getBoolean("max"),
        amount = getOptionalInt("amount"),
        startsOn = getRequiredLocalDate("starts_on")
    )

private fun Income.toMap(familyId: UUID) =
    mapOf(
        "id" to id,
        "family_id" to familyId,
        "max" to max,
        "amount" to amount,
        "starts_on" to startsOn
    )