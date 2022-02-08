package se.pamisoft.theinvoice

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class GuardianRepository(private val db: NamedParameterJdbcTemplate) {
    fun upsert(guardians: Iterable<Guardian>) =
        db.batchUpdate("""
            |insert into guardian (id, first_name, last_name)
            |values (:id, :first_name, :last_name)
            |on conflict (id) do update 
            |set first_name = excluded.first_name, 
            |    last_name = excluded.last_name""".trimMargin(),
            guardians.map { it.toMap() }.toTypedArray()
        )

    fun deleteOrphans() =
        db.jdbcTemplate.update("""
            |delete from guardian 
            |where id not in (
            |    select guardian1_id from family 
            |    union select guardian2_id from family where guardian2_id is not null)""".trimMargin())
}

private fun Guardian.toMap() =
    mapOf("id" to id, "first_name" to firstName, "last_name" to lastName)