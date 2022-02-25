package se.pamisoft.theinvoice

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import se.pamisoft.theinvoice.Delivery.POST
import java.sql.ResultSet
import java.time.LocalDate
import java.util.UUID

@Repository
class FamilyRepository(private val db: NamedParameterJdbcTemplate, private val guardianRepository: GuardianRepository) {
    fun findAll(): Iterable<Family> =
        db.query(QUERY) { rs, _ -> rs.getFamily() }

    fun findById(id: UUID): Family? =
        db.query("$QUERY where f.id = :id", mapOf("id" to id)) { rs, _ -> rs.getFamily() }
            .singleOrNull()

    fun upsert(family: Family) {
        guardianRepository.upsert(family.guardians)

        db.update("""
            |insert into family (
            |    id, 
            |    guardian1_id, 
            |    guardian2_id, 
            |    personal_identity_number, 
            |    delivery, 
            |    email, 
            |    address, 
            |    zip_code, 
            |    city, 
            |    customer_number, 
            |    ended_on)
            |values (
            |    :id, 
            |    :guardian1_id, 
            |    :guardian2_id, 
            |    :personal_identity_number, 
            |    :delivery::delivery, 
            |    :email, 
            |    :address, 
            |    :zip_code, 
            |    :city, 
            |    :customer_number, 
            |    :ended_on)
            |on conflict (id) do update
            |set guardian2_id = :guardian2_id,
            |    personal_identity_number = :personal_identity_number, 
            |    delivery = :delivery::delivery, 
            |    email = :email, 
            |    address = :address,
            |    zip_code =:zip_code,
            |    city = :city,
            |    customer_number = :customer_number, 
            |    ended_on = :ended_on""".trimMargin(),
            family.toMap()
        )

        if (family.guardian2 == null) guardianRepository.deleteOrphans()
    }
}

private val QUERY = """
    |select f.*, 
    |    g1.first_name as guardian1_first_name, g1.last_name as guardian1_last_name, 
    |    g2.first_name as guardian2_first_name, g2.last_name as guardian2_last_name
    |from family f 
    |join guardian g1 on g1.id = f.guardian1_id
    |left join guardian g2 on g2.id = f.guardian2_id""".trimMargin()

private fun Family.toMap() = mapOf(
    "id" to id,
    "guardian1_id" to guardian1.id,
    "guardian2_id" to guardian2?.id,
    "personal_identity_number" to personalIdentityNumber.value,
    "delivery" to delivery.value,
    "email" to email,
    "address" to address?.address,
    "zip_code" to address?.zipCode,
    "city" to address?.city,
    "customer_number" to customerNumber,
    "ended_on" to endedOn
)

private fun ResultSet.getFamily(): Family {
    val delivery = Delivery.of(getString("delivery"))
    return Family(
        id = getRequiredUuid("id"),
        guardian1 = getGuardian(1),
        guardian2 = getUuid("guardian2_id")?.let { getGuardian(2) },
        personalIdentityNumber = PersonalIdentityNumber(getString("personal_identity_number")),
        delivery = delivery,
        email = getString("email"),
        address = if (delivery == POST) getAddress() else null,
        customerNumber = getString("customer_number"),
        endedOn = getObject("ended_on", LocalDate::class.java)
    )
}

private fun ResultSet.getGuardian(index: Int) =
    Guardian(
        id = getRequiredUuid("guardian${index}_id"),
        firstName = getString("guardian${index}_first_name"),
        lastName = getString("guardian${index}_last_name")
    )

private fun ResultSet.getAddress() =
    Address(
        address = getString("address"),
        zipCode = getString("zip_code"),
        city = getString("city")
    )