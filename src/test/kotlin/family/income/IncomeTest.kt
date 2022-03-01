package se.pamisoft.theinvoice.family.income

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import se.pamisoft.theinvoice.NOW

class IncomeTest {
    @Test
    fun `Should fail on missing amount`() {
        val exception =
            assertThrows<IllegalArgumentException> { Income(max = false, amount = null, startsOn = NOW) }

        assertThat(exception.message).isEqualTo("Either supply max or an positive amount.")
    }

    @Test
    fun `Should fail on negative amount`() {
        val exception =
            assertThrows<IllegalArgumentException> { Income(max = false, amount = -1, startsOn = NOW) }

        assertThat(exception.message).isEqualTo("Either supply max or an positive amount.")
    }

    @Test
    fun `Should fail on max with amount`() {
        val exception =
            assertThrows<IllegalArgumentException> { Income(max = true, amount = 40_000, startsOn = NOW) }

        assertThat(exception.message).isEqualTo("Amount should be null for max income.")
    }
}