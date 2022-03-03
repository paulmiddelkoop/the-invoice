package se.pamisoft.theinvoice.family

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DeliveryTest {
    @Test
    fun `Should fail on unknown delivery`() {
        val exception = assertThrows<IllegalArgumentException> {  Delivery.of("unknown") }

        assertThat(exception.message).isEqualTo("Unknown delivery: 'unknown'.")
    }
}