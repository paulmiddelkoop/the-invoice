package se.pamisoft.theinvoice.fortnox

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.PropertyNamingStrategies.UPPER_CAMEL_CASE
import com.fasterxml.jackson.databind.deser.std.StringDeserializer
import io.netty.handler.logging.LogLevel
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.client.reactive.ClientHttpConnector
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import reactor.netty.http.client.HttpClient
import reactor.netty.transport.logging.AdvancedByteBufFormat
import java.net.URI


const val BLANK_VALUE = "API_BLANK"

@Configuration
@ConfigurationPropertiesScan
class FortnoxConfiguration {
    @Bean
    fun webClientConfigurer(properties: FortnoxProperties): WebClientCustomizer {
        val httpClient: HttpClient = HttpClient.create()
            .wiretap(this.javaClass.canonicalName, LogLevel.INFO, AdvancedByteBufFormat.TEXTUAL)
        val conn: ClientHttpConnector = ReactorClientHttpConnector(httpClient)

        return WebClientCustomizer { webClientBuilder ->
            webClientBuilder
                .clientConnector(conn)
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .defaultHeader("Access-Token", properties.accessToken)
                .defaultHeader("Client-Secret", properties.clientSecret)
                .codecs { it.defaultCodecs().jackson2JsonDecoder(Jackson2JsonDecoder(objectMapper())) }
                .codecs { it.defaultCodecs().jackson2JsonEncoder(Jackson2JsonEncoder(objectMapper())) }
        }
    }

    private fun objectMapper(): ObjectMapper =
        Jackson2ObjectMapperBuilder.json()
            .propertyNamingStrategy(UPPER_CAMEL_CASE)
            .deserializers(EmptyStringToNullDeserializer)
            .build<ObjectMapper>()
            .apply { serializerProvider.setNullValueSerializer(ApiBlankSerializer) }
}

@ConstructorBinding
@ConfigurationProperties(prefix = "fortnox")
data class FortnoxProperties(
    val accessToken: String,
    val clientSecret: String,
    val url: URI
)

/**
 * To clear a value in Fortnox we need to use `API_BLANK`.
 * See [Fortnox docs](https://developer.fortnox.se/general/general/#Delete-values) for details.
 */
object ApiBlankSerializer : JsonSerializer<Any?>() {
    override fun serialize(value: Any?, generator: JsonGenerator, serializers: SerializerProvider): Unit =
        generator.writeString(BLANK_VALUE)
}

/**
 * Fortnox sends empty Strings instead of null values.
 */
object EmptyStringToNullDeserializer : JsonDeserializer<String>() {
    override fun deserialize(parser: JsonParser, context: DeserializationContext): String? =
        StringDeserializer.instance.deserialize(parser, context).takeUnless { it.isBlank() }

    override fun handledType(): Class<*> = String::class.java
}