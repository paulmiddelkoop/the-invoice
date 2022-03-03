package se.pamisoft.theinvoice.family

import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import se.pamisoft.theinvoice.fortnox.SyncCustomer
import java.time.LocalDate
import java.util.UUID
import java.util.UUID.randomUUID

@RestController
@RequestMapping("/api/v1/families")
class FamilyController(private val familyRepository: FamilyRepository, private val syncCustomer: SyncCustomer) {

    @GetMapping
    fun all(): List<Family> =
        familyRepository.findAll().sortedBy { it.name }

    @GetMapping("/{id}")
    fun one(@PathVariable id: UUID): Family =
        familyRepository.findById(id) ?: throw ResponseStatusException(NOT_FOUND, "Unknown family $id.")

    @PutMapping("/{id}")
    fun replace(@PathVariable id: UUID, @RequestBody familyDto: FamilyDto): Family =
        if (id != familyDto.id)
            throw ResponseStatusException(BAD_REQUEST, "Path id $id does not equal body id ${familyDto.id}.")
        else
            syncCustomer(familyDto)
                .let { customerNumber -> familyDto.toFamily(customerNumber) }
                .also { family -> familyRepository.upsert(family) }
}

data class FamilyDto(
    val id: UUID = randomUUID(),
    val guardian1: Guardian,
    val guardian2: Guardian?,
    val personalIdentityNumber: PersonalIdentityNumber,
    val email: String,
    val delivery: Delivery,
    val address: Address?,
    val customerNumber: CustomerNumber?,
    val endedOn: LocalDate? = null
) {
    val name = familyName(guardian1, guardian2)

    fun toFamily(customerNumber: CustomerNumber) =
        Family(id, guardian1, guardian2, personalIdentityNumber, email, delivery, address, customerNumber, endedOn)
}