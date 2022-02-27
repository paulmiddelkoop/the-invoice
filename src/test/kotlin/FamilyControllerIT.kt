package se.pamisoft.theinvoice

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.put
import se.pamisoft.theinvoice.Delivery.POST
import se.pamisoft.theinvoice.fortnox.SyncCustomer
import java.util.UUID.randomUUID

@WebMvcTest(FamilyController::class)
class FamilyControllerIT(@Autowired private val mvc: MockMvc, @Autowired private val objectMapper: ObjectMapper) {

    @MockBean
    private lateinit var familyRepository: FamilyRepository

    @MockBean
    private lateinit var syncCustomer: SyncCustomer

    @Test
    fun `Should replace`() {
        val family = family(id = FAMILY_ID1)
        val familyWithExternalReference = family.copy(customerNumber = "89")
        given(syncCustomer(family)).willReturn(familyWithExternalReference)

        val result = mvc.put("/api/v1/families/$FAMILY_ID1") {
            contentType = APPLICATION_JSON
            content = objectMapper.writeValueAsString(family)
        }

        result.andExpect { status { isOk() } }
        then(syncCustomer).should().invoke(family)
        then(familyRepository).should().upsert(familyWithExternalReference)
    }

    @Test
    fun `Should fail replace when id in body does not match id in path`() {
        val result = mvc.put("/api/v1/families/$FAMILY_ID1") {
            contentType = APPLICATION_JSON
            content = objectMapper.writeValueAsString(family(id = FAMILY_ID2))
        }

        result.andExpect { status { isBadRequest() } }
    }

    @Test
    fun `Should return 404 when requesting unknown family`() {
        given(familyRepository.findById(FAMILY_ID1)).willReturn(null)

        mvc.get("/api/v1/families/${randomUUID()}")
            .andExpect { status { isNotFound() } }
    }

    @Test
    fun `Should return family when requesting known family with required fields`() {
        given(familyRepository.findById(FAMILY_ID1)).willReturn(singleParentFamily(id = FAMILY_ID1))

        val result = mvc.get("/api/v1/families/$FAMILY_ID1")

        result.thenJsonResultIs(
            """
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
              "incomes": [],
              "singleParent": true
            }"""
        )
    }

    @Test
    fun `Should return family when requesting known family with all fields`() {
        given(familyRepository.findById(FAMILY_ID1)).willReturn(
            family(
                id = FAMILY_ID1,
                delivery = POST,
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
                "address": "Main street 42",
                "zipCode": "10014",
                "city": "New York"
              },
              "customerNumber": "80",
              "endedOn": $NOW,
              "incomes": [],
              "singleParent": false
            }"""
        )
    }

    @Test
    fun `Should return families when requesting all families`() {
        given(familyRepository.findAll()).willReturn(listOf(family(id = FAMILY_ID1), singleParentFamily(id = FAMILY_ID2)))

        val result = mvc.get("/api/v1/families")

        result.thenJsonResultIs("""
            [
              {
                "id": $FAMILY_ID1,
                "name": "John & Jane Doe",
                "guardian1": {
                  "id": $GUARDIAN_ID1,
                  "firstName": "John",
                  "lastName": "Doe"
                },
                "guardian2": {
                  "id": $GUARDIAN_ID2,
                  "firstName": "Jane",
                  "lastName": "Doe"
                },
                "personalIdentityNumber": "19890201-3286",
                "delivery": "E_INVOICE",
                "email": "john@gmail.com",
                "address": null,
                "customerNumber": "80",
                "endedOn": null,
                "incomes": [],
                "singleParent": false
              },
              {
                "id": $FAMILY_ID2,
                "name": "John Doe",
                "guardian1": {
                  "id": $GUARDIAN_ID1,
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
                "incomes": [],
                "singleParent": true
              }
            ]
            """
        )
    }
}

private fun ResultActionsDsl.thenJsonResultIs(jsonContent: String) {
    andExpect {
        status { isOk() }
        content {
            contentType(APPLICATION_JSON)
            json(jsonContent, strict = true)
        }
    }
}