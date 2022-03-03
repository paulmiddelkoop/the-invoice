package se.pamisoft.theinvoice.family

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
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.put
import se.pamisoft.theinvoice.*
import se.pamisoft.theinvoice.family.Delivery.E_INVOICE
import se.pamisoft.theinvoice.family.Delivery.POST
import se.pamisoft.theinvoice.fortnox.SyncCustomer
import java.util.UUID.randomUUID

@WebMvcTest(FamilyController::class)
class FamilyControllerIT(@Autowired private val mvc: MockMvc, @Autowired private val objectMapper: ObjectMapper) {

    @MockBean
    private lateinit var familyRepository: FamilyRepository

    @MockBean
    private lateinit var syncCustomer: SyncCustomer

    @Nested
    @DisplayName("When replacing")
    inner class WhenReplacing {
        @Test
        fun `should succeed for valid family`() {
            val familyDto = familyDto()
            given(syncCustomer(familyDto)).willReturn(CUSTOMER_NUMBER)

            val result = mvc.put("/api/v1/families/${familyDto.id}") {
                contentType = APPLICATION_JSON
                content = objectMapper.writeValueAsString(familyDto)
            }

            result.andExpect { status { isOk() } }
            then(syncCustomer).should().invoke(familyDto)
            then(familyRepository).should().upsert(familyDto.toFamily(CUSTOMER_NUMBER))
        }

        @Test
        fun `should fail when id in body does not match id in path`() {
            val result = mvc.put("/api/v1/families/$FAMILY_ID1") {
                contentType = APPLICATION_JSON
                content = objectMapper.writeValueAsString(family(id = FAMILY_ID2))
            }

            result.andExpect { status { isBadRequest() } }
        }
    }

    @Nested
    @DisplayName("When get one")
    inner class WhenGetOne {
        @Test
        fun `should return 404 when requesting unknown family`() {
            val result = mvc.get("/api/v1/families/${randomUUID()}")

            result.andExpect { status { isNotFound() } }
        }

        @Test
        fun `should return family when requesting known family with required fields`() {
            given(familyRepository.findById(FAMILY_ID1)).willReturn(familyWithRequiredFields())

            val result = mvc.get("/api/v1/families/$FAMILY_ID1")

            result.thenJsonResultIs(JSON_REQUIRED_FIELDS)
        }

        @Test
        fun `should return family when requesting known family with all fields`() {
            given(familyRepository.findById(FAMILY_ID1)).willReturn(
                family(
                    id = FAMILY_ID1,
                    guardian1 = guardian("John Doe", GUARDIAN_ID1),
                    guardian2 = guardian("Jane Doe", GUARDIAN_ID2),
                    personalIdentityNumber = PersonalIdentityNumber("19890201-3286"),
                    email = "john@gmail.com",
                    delivery = POST,
                    address = address("Main Street 42", "10014", "New York"),
                    customerNumber = CustomerNumber("80"),
                    endedOn = NOW
                )
            )
            val result = mvc.get("/api/v1/families/$FAMILY_ID1")

            result.thenJsonResultIs(
                """
                {
                  "id": "$FAMILY_ID1",
                  "name": "John & Jane Doe",
                  "guardian1": {
                    "id": "$GUARDIAN_ID1",
                    "firstName": "John",
                    "lastName": "Doe"
                  },
                  "guardian2": {
                    "id": "$GUARDIAN_ID2",
                    "firstName": "Jane",
                    "lastName": "Doe"
                  },
                  "personalIdentityNumber": "19890201-3286",
                  "delivery": "POST",
                  "email": "john@gmail.com",
                  "address": {
                    "address": "Main Street 42",
                    "zipCode": "10014",
                    "city": "New York"
                  },
                  "customerNumber": "80",
                  "endedOn": $NOW,
                  "singleParent": false
                }"""
            )
        }
    }

    @Test
    fun `Should return families when requesting all families`() {
        given(familyRepository.findAll()).willReturn(listOf(familyWithRequiredFields()))

        val result = mvc.get("/api/v1/families")

        result.thenJsonResultIs("[$JSON_REQUIRED_FIELDS]")
    }

    private fun familyWithRequiredFields() =
        singleParentFamily(
            id = FAMILY_ID1,
            guardian = guardian("John Doe", GUARDIAN_ID1),
            personalIdentityNumber = PersonalIdentityNumber("19890201-3286"),
            email = "john@gmail.com",
            delivery = E_INVOICE,
            customerNumber = CustomerNumber("80")
        )

    private companion object {
        val JSON_REQUIRED_FIELDS = """                
                {
                  "id": "$FAMILY_ID1",
                  "name": "John Doe",
                  "guardian1": {
                    "id": "$GUARDIAN_ID1",
                    "firstName": "John",
                    "lastName": "Doe"
                  },
                  "guardian2": null,
                  "personalIdentityNumber": "19890201-3286",
                  "delivery": "E_INVOICE",
                  "email": "john@gmail.com",
                  "address": null,
                  "customerNumber": "80",
                  "endedOn": null,
                  "singleParent": true
                }"""
    }
}