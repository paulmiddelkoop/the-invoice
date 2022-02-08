package se.pamisoft.theinvoice

import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.get
import se.pamisoft.theinvoice.Delivery.POST
import se.pamisoft.theinvoice.development.*
import java.util.UUID

@WebMvcTest(FamilyController::class)
class FamilyControllerIT(@Autowired private val mvc: MockMvc) {

    @MockBean
    private lateinit var familyRepository: FamilyRepository

    @Test
    fun `Should return 404 when requesting unknown family`() {
        given(familyRepository.findById(FAMILY_ID1)).willReturn(null)

        mvc.get("/api/v1/families/${UUID.randomUUID()}") {}.andExpect { status { isNotFound() } }
    }

    @Test
    fun `Should return family when requesting known family with required fields`() {
        given(familyRepository.findById(FAMILY_ID1)).willReturn(singleParentFamily(id = FAMILY_ID1))

        mvc.get("/api/v1/families/$FAMILY_ID1").andExpectJson("""
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
              "externalReference": null,
              "endedOn": null
            }"""
        )
    }

    @Test
    fun `Should return family when requesting known family with all fields`() {
        given(familyRepository.findById(FAMILY_ID1)).willReturn(
            family(
                id = FAMILY_ID1,
                delivery = POST,
                externalReference = "E1",
                endedOn = NOW
            )
        )

        mvc.get("/api/v1/families/$FAMILY_ID1").andExpectJson("""
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
              "externalReference": E1,
              "endedOn": $NOW
            }"""
        )
    }
}

private fun ResultActionsDsl.andExpectJson(jsonContent: String) {
    andExpect {
        status { isOk() }
        content {
            contentType(APPLICATION_JSON)
            json(jsonContent, strict = true)
        }
    }
}