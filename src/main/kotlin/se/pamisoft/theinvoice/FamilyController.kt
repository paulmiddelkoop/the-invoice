package se.pamisoft.theinvoice

import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@RestController
@RequestMapping("/api/v1/families")
class FamilyController(private val familyRepository: FamilyRepository) {

    @GetMapping("/")
    fun all(): List<Family> =
        familyRepository.findAll().sortedBy { it.name }

    @GetMapping("/{id}")
    fun one(@PathVariable id: UUID): Family =
        familyRepository.findById(id) ?: throw ResponseStatusException(NOT_FOUND, "Unknown family $id.");

    // TODO: check guardian id is not changed
    @PutMapping("/{id}")
    fun replace(@PathVariable id: UUID, @RequestBody family: Family) {
        if (id != family.id)
            throw ResponseStatusException(BAD_REQUEST, "Path id $id does not equal body id ${family.id}.")
        familyRepository.upsert(family)
    }
}