package se.pamisoft.theinvoice.family.child

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import se.pamisoft.theinvoice.CHILD_ID
import se.pamisoft.theinvoice.child
import se.pamisoft.theinvoice.family
import se.pamisoft.theinvoice.family.Family
import se.pamisoft.theinvoice.family.FamilyRepository
import se.pamisoft.theinvoice.thenJsonResultIs
import java.time.LocalDate
import java.util.UUID

@WebMvcTest(ChildController::class)
class ChildControllerIT(@Autowired private val mvc: MockMvc) {

    @MockBean
    private lateinit var childRepository: ChildRepository

    @MockBean
    private lateinit var familyRepository: FamilyRepository

    @Nested
    @DisplayName("When getting for family")
    inner class WhenGettingForFamily {
        @Test
        fun `should return children when family has children`() {
            val family = givenFamilyExists(family())
            given(childRepository.findFor(family)).willReturn(
                listOf(
                    child(
                        firstName = "Molly",
                        lastName = "Doe",
                        bornOn = LocalDate.of(2010, 10, 30),
                        parents = family,
                        id = CHILD_ID
                    )
                )
            )

            val result = mvc.get("/api/v1/families/${family.id}/children")

            result.thenJsonResultIs("""
                [
                  {
                    "id": "$CHILD_ID",
                    "firstName": "Molly",
                    "lastName": "Doe",
                    "bornOn": "2010-10-30",
                    "name": "Molly Doe"
                  }
                ]  
            """)
        }

        @Test
        fun `should return empty list when family has no children`() {
            val family = givenFamilyExists(family())

            val result = mvc.get("/api/v1/families/${family.id}/children")

            result.thenJsonResultIs("[]")
        }

        @Test
        fun `should return 500 when requesting unknown family`() {
            val result = mvc.get("/api/v1/families/${UUID.randomUUID()}/children")

            result.andExpect { status { isBadRequest() } }
        }

        private fun givenFamilyExists(family: Family) =
            given(familyRepository.findById(family.id)).willReturn(family).let { family }
    }
}