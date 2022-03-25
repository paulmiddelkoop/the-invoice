package se.pamisoft.theinvoice.family.child

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import se.pamisoft.theinvoice.child

class ChildTest {
    @Test
    fun `Should concat first and last name`() {
        val child = child(firstName = "Paul", lastName = "Middelkoop")

        assertThat(child.name).isEqualTo("Paul Middelkoop")
    }
}