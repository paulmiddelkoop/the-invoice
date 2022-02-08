package se.pamisoft.theinvoice

import dev.personnummer.PersonnummerException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PersonalIdentityNumberTest {
    @Test
    fun `Should fail creation on incorrect number`() {
        assertThrows< PersonnummerException> { PersonalIdentityNumber("19770622-1234") }
    }

    @Test
    fun `Should format to long format`() {
        val personalIdentityNumber = PersonalIdentityNumber("890201-3286")

        assertThat(personalIdentityNumber.value).isEqualTo("19890201-3286")
    }
}