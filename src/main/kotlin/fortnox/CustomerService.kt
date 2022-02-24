package se.pamisoft.theinvoice.fortnox

import mu.KotlinLogging.logger
import org.springframework.http.HttpMethod.POST
import org.springframework.http.HttpMethod.PUT
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

private val logger = logger {}

@Service
class CustomerService(webClientBuilder: WebClient.Builder, properties: FortnoxProperties) {
    private val webClient = webClientBuilder.baseUrl("${properties.url}/customers").build()

    suspend fun createOrUpdate(customer: Customer): Customer {
        val httpMethod = if (customer.customerNumber == BLANK_VALUE) POST else PUT
        logger.info { "$httpMethod customer ${customer.name} in Fortnox." }

        return webClient
            .method(httpMethod)
            .bodyValue(CustomerHolder(customer))
            .retrieve()
            .awaitBody<CustomerHolder>().customer
    }
}

data class CustomerHolder(val customer: Customer)
