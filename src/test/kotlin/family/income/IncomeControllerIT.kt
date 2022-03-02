package se.pamisoft.theinvoice.family.income

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.put
import se.pamisoft.theinvoice.*
import se.pamisoft.theinvoice.family.FamilyRepository
import java.time.LocalDate

@WebMvcTest(IncomeController::class)
class IncomeControllerIT(@Autowired private val mvc: MockMvc, @Autowired private val objectMapper: ObjectMapper) {

    @MockBean
    private lateinit var incomeRepository: IncomeRepository

    @MockBean
    private lateinit var familyRepository: FamilyRepository

    @Nested
    @DisplayName("When replacing")
    inner class WhenReplacing {
        @Test
        fun `should succeed for valid income`() {
            val income = maxIncome(id = INCOME_ID)

            val result = mvc.put("/api/v1/families/$FAMILY_ID1/incomes/$INCOME_ID") {
                contentType = APPLICATION_JSON
                content = objectMapper.writeValueAsString(income)
            }

            result.andExpect { status { isOk() } }
            then(incomeRepository).should().upsert(income, FAMILY_ID1)
        }

        @Test
        fun `should fail when id in body does not match id in path`() {
            val result = mvc.put("/api/v1/families/$FAMILY_ID1/incomes/$INCOME_ID") {
                contentType = APPLICATION_JSON
                content = objectMapper.writeValueAsString(maxIncome(id = FAMILY_ID2))
            }

            result.andExpect { status { isBadRequest() } }
        }
    }

    @Nested
    @DisplayName("When getting")
    inner class WhenGetting {
        @Test
        fun `should return incomes when exist`() {
            given(familyRepository.exist(FAMILY_ID1)).willReturn(true)
            given(incomeRepository.findFor(FAMILY_ID1)).willReturn(
                listOf(
                    income(
                        id = INCOME_ID,
                        amount = 40_000,
                        changedOn = LocalDate.of(2021, 2, 28)
                    )
                )
            )

            val result = mvc.get("/api/v1/families/$FAMILY_ID1/incomes")

            result.thenJsonResultIs(
                """
                [
                    {
                        "id": "$INCOME_ID",
                        "max": false,
                        "amount": 40000,
                        "startsOn": "2021-02-28"
                    }
                ]
                """
            )
        }

        @Test
        fun `should return empty list when no income exist`() {
            given(familyRepository.exist(FAMILY_ID1)).willReturn(true)

            val result = mvc.get("/api/v1/families/$FAMILY_ID1/incomes")

            result.thenJsonResultIs("[]")
        }

        @Test
        fun `should fail when family does not exist`() {
            given(familyRepository.exist(FAMILY_ID1)).willReturn(false)

            val result = mvc.get("/api/v1/families/$FAMILY_ID1/incomes")

            result.andExpect { status { isNotFound() } }
        }
    }

    @Nested
    @DisplayName("When deleting")
    inner class WhenDeleting {
        @Test
        fun `should delete income when exist`() {
            given(familyRepository.exist(FAMILY_ID1)).willReturn(true)

            val result = mvc.delete("/api/v1/families/$FAMILY_ID1/incomes/$INCOME_ID")

            result.andExpect { status { isOk() } }
            then(incomeRepository).should().delete(INCOME_ID)
        }

        @Test
        fun `should fail when family does not exist`() {
            given(familyRepository.exist(FAMILY_ID1)).willReturn(false)

            val result = mvc.delete("/api/v1/families/$FAMILY_ID1/incomes/$INCOME_ID")

            result.andExpect { status { isNotFound() } }
        }
    }
}