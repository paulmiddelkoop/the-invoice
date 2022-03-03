package se.pamisoft.theinvoice.family

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import se.pamisoft.theinvoice.*

@RepositoryTest
class ChildRepositoryIT(
    @Autowired private val childRepository: ChildRepository,
    @Autowired private val familyRepository: FamilyRepository
) {
    @Nested
    @DisplayName("When upserting")
    inner class WhenUpserting {
        @Test
        fun `should insert`() {
            val parents = givenFamily(family())
            val child = child(parents = parents)

            childRepository.upsert(child)

            childRepository.findForFamily(parents).contains(child)

        }

        @Test
        fun `should update`() {
            val child = givenChild(child())
            val changedChild = child.copy(firstName = "Other", lastName = "Name", bornOn = child.bornOn.minusYears(1))

            childRepository.upsert(changedChild)

            childRepository.findForFamily(changedChild.parents).contains(changedChild)
        }
    }

    @Nested
    @DisplayName("When finding for family")
    inner class WhenFindingForFamily {
        @Test
        fun `should find children`() {
            val parents = family()
            val child1 = givenChild(child("Karen", parents = parents))
            val child2 = givenChild(child("Julie", parents = parents))

            val children = childRepository.findForFamily(parents)

            assertThat(children).containsExactlyInAnyOrder(child1, child2)
        }

        @Test
        fun `should find children from current and previous relationships`() {
            val currentRelation = singleParentFamily()
            val firstRelation = family(guardian1 = currentRelation.guardian1, endedOn = NOW.minusYears(1))
            val secondRelation = family(
                guardian1 = guardian("Susan Black"),
                guardian2 = currentRelation.guardian1,
                endedOn = NOW.minusMonths(1)
            )
            val child1 = givenChild(child("Karen", parents = firstRelation))
            val child2 = givenChild(child("Julie", parents = secondRelation))

            val children = childRepository.findForFamily(currentRelation)

            assertThat(children).containsExactlyInAnyOrder(child1, child2)
        }
    }

    private fun givenChild(child: Child) =
        givenFamily(child.parents).also { childRepository.upsert(child) }.let { child }


    private fun givenFamily(family: Family) =
        familyRepository.upsert(family).let { family }
}