package se.pamisoft.theinvoice.family

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere
import se.pamisoft.theinvoice.*
import se.pamisoft.theinvoice.family.Delivery.POST
import java.time.LocalDate.now
import java.util.UUID.randomUUID

@RepositoryTest
class FamilyRepositoryIT(@Autowired private val repository: FamilyRepository, @Autowired private val db: JdbcTemplate) {
    @Nested
    @DisplayName("When upserting")
    inner class WhenUpserting {
        @Test
        fun `should insert when family is new and has required fields`() {
            val family = singleParentFamily()

            repository.upsert(family)

            assertThat(stored(family)).isEqualTo(family)
        }

        @Test
        fun `should insert when family is new and has all fields`() {
            val family = family(delivery = POST, address = address(), endedOn = now())

            repository.upsert(family)

            assertThat(stored(family)).isEqualTo(family)
        }

        @Test
        fun `should update when family exists and is changed`() {
            val family = givenFamily(family(endedOn = null))

            repository.upsert(family.copy(endedOn = NOW))

            assertThat(stored(family)?.endedOn).isEqualTo(NOW)
        }

        @Test
        fun `should remove orphan guardian`() {
            val family = givenFamily(family(guardian2 = guardian(id = GUARDIAN_ID2)))

            repository.upsert(family.copy(guardian2 = null))

            assertThat(countRowsInTableWhere(db, "guardian", "id = '$GUARDIAN_ID2'")).isEqualTo(0)
        }

        private fun stored(family: Family) = repository.findById(family.id)
    }

    @Test
    fun `Should find all`() {
        val family1 = givenFamily(family())
        val family2 = givenFamily(singleParentFamily())

        val families = repository.findAll()

        assertThat(families).containsExactlyInAnyOrder(family1, family2)
    }

    @Nested
    @DisplayName("WhenExists")
    inner class WhenExists {
        @Test
        fun `should return true if exists`() {
            val family = givenFamily(family())

            val exists = repository.exist(family.id)

            assertThat(exists).isTrue
        }

        @Test
        fun `should return false if not exists`() {
            val exists = repository.exist(randomUUID())

            assertThat(exists).isFalse
        }
    }

    private fun givenFamily(family: Family) =
        repository.upsert(family).let { family }
}