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
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@SpringBootTest
class FortnoxConfigurationIT(@Autowired webClientBuilder: WebClient.Builder) {
    private val webClient = webClientBuilder.build()

    @Test
    fun `Should add required Fortnox headers`() {
        mockWebServer.givenJsonResponse("""{"Field": "value"}""")

        runBlocking { webClient.get().uri(mockUrl()).retrieve().awaitBody<TestEntity>() }

        val request = mockWebServer.takeRequest()
        assertThat(request.headers).contains(
            "Access-Token" to "x",
            "Client-Secret" to "y",
            CONTENT_TYPE to APPLICATION_JSON_VALUE
        )
    }

    @Test
    fun `Should convert null to API_BLANK`() {
        mockWebServer.givenJsonResponse("""{"Field": "API_BLANK"}""")
        val testEntity = TestEntity(null)

        runBlocking { webClient.post().uri(mockUrl()).bodyValue(testEntity).retrieve().awaitBody<TestEntity>() }

        val request = mockWebServer.takeRequest()
        assertThat(request.body.readUtf8()).isEqualTo("""{"Field":"API_BLANK"}""")
    }

    private fun mockUrl() = mockWebServer.url("/").toString()

    companion object {
        val mockWebServer = MockWebServer().apply { start() }

        @AfterAll
        @JvmStatic
        fun stopMockWebServer() {
            mockWebServer.shutdown()
        }
    }
}

private fun MockWebServer.givenJsonResponse(json: String) =
    enqueue(
        MockResponse()
            .setBody(json)
            .addHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
    )


private data class TestEntity(val field: String?)