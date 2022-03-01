package se.pamisoft.theinvoice.family.income

import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import se.pamisoft.theinvoice.family.FamilyRepository
import java.util.UUID

@RestController
@RequestMapping("/api/v1/families/{familyId}/incomes")
class IncomeController(private val incomeRepository: IncomeRepository, private val familyRepository: FamilyRepository) {
    @GetMapping
    fun getForFamily(@PathVariable familyId: UUID): List<Income> =
        ensureFamilyExists(familyId).run { incomeRepository.findFor(familyId) }

    @PutMapping("/{id}")
    fun replace(@PathVariable familyId: UUID, @PathVariable id: UUID, @RequestBody income: Income) {
        if (id != income.id)
            throw ResponseStatusException(BAD_REQUEST, "Path id $id does not equal body id ${income.id}.")

        incomeRepository.upsert(income, familyId)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable familyId: UUID, @PathVariable id: UUID) {
        ensureFamilyExists(familyId).run { incomeRepository.delete(id) }
    }

    private fun ensureFamilyExists(familyId: UUID) {
        if (!familyRepository.exist(familyId)) throw ResponseStatusException(NOT_FOUND, "Unknown family $familyId.")
    }
}