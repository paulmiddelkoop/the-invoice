package se.pamisoft.theinvoice.family.income

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import se.pamisoft.theinvoice.*
import se.pamisoft.theinvoice.family.FamilyRepository

@RepositoryTest
class IncomeRepositoryIT(
    @Autowired private val incomeRepository: IncomeRepository,
    @Autowired private val familyRepository: FamilyRepository
) {
    @BeforeEach
    fun insertFamily() {
        familyRepository.upsert(family(id = FAMILY_ID1))
    }

    @Nested
    @DisplayName("When upsert")
    inner class WhenUpsert {
        @Test
        fun `should insert max income`() {
            val maxIncome = maxIncome()

            incomeRepository.upsert(maxIncome, FAMILY_ID1)

            assertThat(incomeRepository.findFor(FAMILY_ID1)).containsExactly(maxIncome)
        }

        @Test
        fun `should insert income with amount`() {
            val income = income(40_000)

            incomeRepository.upsert(income, FAMILY_ID1)

            assertThat(incomeRepository.findFor(FAMILY_ID1)).containsExactly(income)
        }

        @Test
        fun `should upsert income`() {
            givenIncome(maxIncome(id = INCOME_ID))
            val newIncome = income(id = INCOME_ID, changedOn = NOW.minusMonths(1))

            incomeRepository.upsert(newIncome, FAMILY_ID1)

            assertThat(incomeRepository.findFor(FAMILY_ID1)).containsExactly(newIncome)
        }
    }

    @Test
    fun `Should delete income`() {
        givenIncome(maxIncome(id = INCOME_ID))

        incomeRepository.delete(INCOME_ID)

        assertThat(incomeRepository.findFor(FAMILY_ID1)).isEmpty()

    }

    private fun givenIncome(income: Income) {
        incomeRepository.upsert(income, FAMILY_ID1)
    }
}