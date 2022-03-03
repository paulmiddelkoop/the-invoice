package family

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import se.pamisoft.theinvoice.family
import se.pamisoft.theinvoice.family.Delivery.POST
import se.pamisoft.theinvoice.guardian

class FamilyTest {
    @Test
    fun `Should fail when delivery is post and address is missing`() {
        val exception = assertThrows<IllegalArgumentException> { family(delivery = POST, address = null) }

        assertThat(exception.message).isEqualTo("Address is required when using post delivery.")
    }

    @Nested
    @DisplayName("When determining name")
    inner class WhenDeterminingName {
        @Test
        fun `should not repeat last name when last names are equal`() {
            val familyWithSameLastName = family(
                guardian1 = guardian("John Doe"),
                guardian2 = guardian("Jane Doe")
            )

            val name = familyWithSameLastName.name

            assertThat(name).isEqualTo("John & Jane Doe")
        }

        @Test
        fun `should concat when last name are not equal`() {
            val familyWithSameLastName = family(
                guardian1 = guardian("Paul Middelkoop"),
                guardian2 = guardian("Josje de Jong")
            )

            val name = familyWithSameLastName.name

            assertThat(name).isEqualTo("Paul Middelkoop & Josje de Jong")
        }
    }
}