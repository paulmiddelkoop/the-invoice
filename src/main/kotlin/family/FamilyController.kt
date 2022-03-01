package se.pamisoft.theinvoice.family

import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import se.pamisoft.theinvoice.fortnox.SyncCustomer
import java.util.UUID

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
    fun replace(@PathVariable id: UUID, @RequestBody family: Family) {
        if (id != family.id)
            throw ResponseStatusException(BAD_REQUEST, "Path id $id does not equal body id ${family.id}.")

        val syncedFamily = syncCustomer(family)
        familyRepository.upsert(syncedFamily)
    }
}