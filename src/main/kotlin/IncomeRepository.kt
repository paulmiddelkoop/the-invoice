package se.pamisoft.theinvoice

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class IncomeRepository(private val db: NamedParameterJdbcTemplate) {
    fun upsert(family: Family) =
        db.batchUpdate(
            """
            |insert into income (id, family_id, max, amount, changed_on)
            |values (:id, :family_id, :max, :amount, :changed_on)
            |on conflict (id) do update 
            |set max = excluded.max, 
            |    amount = excluded.amount,
            |    changed_on = excluded.changed_on""".trimMargin(),
            family.incomes.map { it.toMap(family) }.toTypedArray()
        )

    fun findFor(family: Family): List<Income> =
        db.query(
            "select * from income where family_id = :family_id order by changed_on desc",
            mapOf("family_id" to family.id)
        ) { rs, _ -> rs.getIncome() }
}

private fun ResultSet.getIncome() =
    Income(
        id = getRequiredUuid("id"),
        max = getBoolean("max"),
        amount = getInt("amount"),
        changedOn = getRequiredLocalDate("changed_on")
    )

private fun Income.toMap(family: Family) =
    mapOf(
        "id" to id,
        "family_id" to family.id,
        "max" to max,
        "amount" to amount,
        "changed_on" to changedOn
    )