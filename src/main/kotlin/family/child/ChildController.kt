package se.pamisoft.theinvoice.family.child

import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import se.pamisoft.theinvoice.family.FamilyRepository
import java.util.UUID

@RestController
@RequestMapping("/api/v1/families/{familyId}/children")
class ChildController(private val childRepository: ChildRepository, private val familyRepository: FamilyRepository) {
    @GetMapping
    fun getForFamily(@PathVariable familyId: UUID): List<Child> =
        familyRepository.findById(familyId)?.let { childRepository.findFor(it) }
            ?: throw ResponseStatusException(BAD_REQUEST, "Unknown family $familyId.")
}
