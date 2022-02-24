package se.pamisoft.theinvoice.fortnox

import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.HttpMethod.POST
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestPropertySource
import se.pamisoft.theinvoice.fortnox.DefaultDeliveryType.ELECTRONICINVOICE


@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = ["fortnox.uri"])
class CustomerServiceIT(@Autowired private val customerService: CustomerService) {
    @Test
    fun `Should create customer`() {
        mockWebServer.givenJsonResponse("/Customer.json")

        val customer = Customer(
            customerNumber = BLANK_VALUE,
            name = "Test",
            organisationNumber = "19770622-3232",
            defaultDeliveryTypes = DefaultDeliveryTypes(ELECTRONICINVOICE),
            address1 = null,
            zipCode = null,
            city = null,
            email = null,
            emailInvoice = null,
            active = true
        )

        val storedCustomer = runBlocking { customerService.createOrUpdate(customer) }

        assertThat(storedCustomer).isEqualTo(customer.copy(customerNumber = "98"))
        with(mockWebServer.takeRequest()) {
            assertThat(method).isEqualTo(POST.name)
            assertThat(path).isEqualTo("/customers")
            assertThat(headers.names()).contains("Access-Token", "Client-Secret", CONTENT_TYPE)
        }
    }

    companion object {
        val mockWebServer = MockWebServer().apply { start() }

        @AfterAll
        @JvmStatic
        fun stopMockWebServer() {
            mockWebServer.shutdown()
        }

        // TODO also set secrets?
        @DynamicPropertySource
        @JvmStatic
        fun overrideFortnoxUri(registry: DynamicPropertyRegistry): Unit =
            registry.add("fortnox.url") { mockWebServer.url("/").toString() }
                .also { println(mockWebServer.url("/").toString()) }
    }
}

fun MockWebServer.givenJsonResponse(fileName: String) =
    enqueue(
        MockResponse()
            .setBody(this::class.java.getResource(fileName)!!.readText())
            .addHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
    )
