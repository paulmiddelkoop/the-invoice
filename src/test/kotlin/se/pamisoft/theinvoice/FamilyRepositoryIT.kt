package se.pamisoft.theinvoice

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere
import se.pamisoft.theinvoice.Delivery.POST
import se.pamisoft.theinvoice.development.*
import java.time.LocalDate.now

@RepositoryTest
class FamilyRepositoryIT(
    @Autowired private val repository: FamilyRepository,
    @Autowired private val db: JdbcTemplate
) {
    @Test
    fun `Should insert family with required fields`() {
        val family = singleParentFamily(id = FAMILY_ID1)

        repository.upsert(family)

        assertThat(repository.findById(FAMILY_ID1)).isEqualTo(family)
    }

    @Test
    fun `Should insert with all fields`() {
        val family =
            family(id = FAMILY_ID1, delivery = POST, address = address(), externalReference = "C1", endedOn = now())

        repository.upsert(family)

        assertThat(repository.findById(FAMILY_ID1)).isEqualTo(family)
    }

    @Test
    fun `Should update ended on`() {
        val family = givenFamily(family(id = FAMILY_ID1, endedOn = null))

        repository.upsert(family.copy(endedOn = NOW))

        assertThat(repository.findById(FAMILY_ID1)?.endedOn).isEqualTo(NOW)
    }

    @Test
    fun `Should remove orphan guardian`() {
        val family = givenFamily(family(guardian2 = guardian(id = GUARDIAN_ID2)))

        repository.upsert(family.copy(guardian2 = null))

        assertThat(countRowsInTableWhere(db, "guardian", "id = '$GUARDIAN_ID2'")).isEqualTo(0)
    }

    @Test
    fun `Should find all`() {
        val family1 = givenFamily(family())
        val family2 = givenFamily(singleParentFamily())

        val families = repository.findAll()

        assertThat(families).containsExactlyInAnyOrder(family1, family2)
    }

    private fun givenFamily(family: Family) =
        repository.upsert(family).let { family }
}