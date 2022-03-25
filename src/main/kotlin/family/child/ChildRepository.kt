package se.pamisoft.theinvoice.family.child

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import se.pamisoft.theinvoice.family.Family
import se.pamisoft.theinvoice.family.FamilyRepository
import se.pamisoft.theinvoice.getRequiredLocalDate
import se.pamisoft.theinvoice.getRequiredUuid
import java.sql.ResultSet

@Repository
class ChildRepository(private val db: NamedParameterJdbcTemplate, private val familyRepository: FamilyRepository) {
    fun upsert(child: Child) =
        db.update(
            """
            |insert into child (id, parents_id, first_name, last_name, born_on)
            |values (:id, :parents_id, :first_name, :last_name, :born_on)
            |on conflict (id) do update 
            |set first_name = excluded.first_name, 
            |    last_name = excluded.last_name,
            |    born_on = excluded.born_on""".trimMargin(),
            child.toMap()
        )

    fun findFor(family: Family): List<Child> =
        db.query(
            """
            |select * from child 
            |where parents_id in (
            |    select id from family f 
            |    where f.guardian1_id = :guardian1_id 
            |        or f.guardian1_id = :guardian2_id 
            |        or f.guardian2_id = :guardian1_id 
            |        or f.guardian2_id = :guardian2_id)   """.trimMargin(),
            mapOf("guardian1_id" to family.guardian1.id, "guardian2_id" to family.guardian2?.id)
        )
        { rs, _ -> rs.getChild() }

    private fun ResultSet.getChild() =
        Child(
            id = getRequiredUuid("id"),
            parents = familyRepository.findById(getRequiredUuid("parents_id"))!!,
            firstName = getString("first_name"),
            lastName = getString("last_name"),
            bornOn = getRequiredLocalDate("born_on")
        )
}


private fun Child.toMap() =
    mapOf(
        "id" to id,
        "parents_id" to parents.id,
        "first_name" to firstName,
        "last_name" to lastName,
        "born_on" to bornOn
    )