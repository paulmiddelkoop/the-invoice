package se.pamisoft.theinvoice.fortnox

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
import se.pamisoft.theinvoice.family


@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = ["fortnox.uri"])
class SyncCustomerIT(@Autowired private val syncCustomer: SyncCustomer) {
    @Test
    fun `Should create customer`() {
        mockWebServer.givenJsonResponse("/customer.json")
        val family = family(customerNumber = null)

        val syncedFamily = syncCustomer(family)

        assertThat(syncedFamily).isEqualTo(family.copy(customerNumber = "98"))
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
