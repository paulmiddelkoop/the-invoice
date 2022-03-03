package se.pamisoft.theinvoice.fortnox

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.HttpMethod.POST
import org.springframework.http.HttpMethod.PUT
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import se.pamisoft.theinvoice.CUSTOMER_NUMBER
import se.pamisoft.theinvoice.address
import se.pamisoft.theinvoice.family.Delivery
import se.pamisoft.theinvoice.family.Delivery.E_INVOICE
import se.pamisoft.theinvoice.family.PersonalIdentityNumber
import se.pamisoft.theinvoice.familyDto
import se.pamisoft.theinvoice.guardian

@SpringBootTest
class SyncCustomerIT(@Autowired private val syncCustomer: SyncCustomer) {
    @Test
    fun `Should create customer`() {
        mockWebServer.givenJsonResponse("/customer.json")
        val family = familyDto(
            guardian1 = guardian("John Doe"),
            guardian2 = guardian("Jane Doe"),
            personalIdentityNumber = PersonalIdentityNumber("19890201-3286"),
            email = "john@gmail.com",
            delivery = Delivery.POST,
            address = address("Main Street 42", "10014", "New York"),
            customerNumber = null
        )

        val customerNumber = syncCustomer(family)

        assertThat(customerNumber).isEqualTo(CUSTOMER_NUMBER)
        with(mockWebServer.takeRequest()) {
            assertThat(method).isEqualTo(POST.name)
            assertThat(path).isEqualTo("/customers")
            assertEquals(
                body.readUtf8(), """  {
                      "Customer": {
                        "CustomerNumber": "API_BLANK",
                        "Name": "John & Jane Doe",
                        "OrganisationNumber": "19890201-3286",
                        "Email": "john@gmail.com",
                        "EmailInvoice": "john@gmail.com",
                        "DefaultDeliveryTypes": {
                          "Invoice": "PRINT"
                        },
                        "Type": "PRIVATE",
                        "Address1": "Main Street 42",
                        "ZipCode": "10014",
                        "City": "New York",
                        "Active": true
                      }
                    }""", true
            )
        }
    }

    @Test
    fun `Should update customer`() {
        mockWebServer.givenJsonResponse("/customer.json")
        val family = familyDto(customerNumber = CUSTOMER_NUMBER, delivery = E_INVOICE)

        val customerNumber = syncCustomer(family)

        assertThat(mockWebServer.takeRequest().method).isEqualTo(PUT.name)
        assertThat(customerNumber).isEqualTo(CUSTOMER_NUMBER)
    }

    @Suppress("unused")
    companion object {
        val mockWebServer = MockWebServer().apply { start() }

        @AfterAll
        @JvmStatic
        fun stopMockWebServer() {
            mockWebServer.shutdown()
        }

        @DynamicPropertySource
        @JvmStatic
        fun overrideFortnoxUri(registry: DynamicPropertyRegistry): Unit =
            registry.add("fortnox.url") { mockWebServer.url("/").toString() }
    }
}

private fun MockWebServer.givenJsonResponse(fileName: String) = enqueue(
    MockResponse().setBody(this::class.java.getResource(fileName)!!.readText())
        .addHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
)