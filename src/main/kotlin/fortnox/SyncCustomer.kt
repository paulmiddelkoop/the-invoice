package se.pamisoft.theinvoice.fortnox

import kotlinx.coroutines.runBlocking
import mu.KotlinLogging.logger
import org.springframework.http.HttpMethod.POST
import org.springframework.http.HttpMethod.PUT
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import se.pamisoft.theinvoice.family.CustomerNumber
import se.pamisoft.theinvoice.family.Delivery
import se.pamisoft.theinvoice.family.FamilyDto
import se.pamisoft.theinvoice.fortnox.CustomerType.PRIVATE

private val logger = logger {}

// TODO do not create customers in Fortnox if they are separated
@Service
class SyncCustomer(webClientBuilder: WebClient.Builder, properties: FortnoxProperties) {
    private val webClient: WebClient = webClientBuilder.baseUrl("${properties.url}/customers").build()

    operator fun invoke(family: FamilyDto): CustomerNumber = runBlocking {
        val httpMethod = if (family.customerNumber == null) POST else PUT
        logger.info { "$httpMethod family ${family.name} in Fortnox." }

        webClient
            .method(httpMethod)
            .uri("/${family.customerNumber}".takeIf { httpMethod == PUT }.orEmpty())
            .bodyValue(CustomerHolder(family.toCustomer(true)))
            .retrieve()
            .awaitBody<CustomerHolder>().customer.customerNumber!!
            .let { CustomerNumber(it) }
    }
}

// TODO fix active
private fun FamilyDto.toCustomer(active: Boolean) =
    Customer(
        customerNumber = customerNumber?.value,
        name = name,
        organisationNumber = personalIdentityNumber.value,
        email = email,
        emailInvoice = email,
        defaultDeliveryTypes = toDefaultDeliveryTypes(),
        address1 = address?.address,
        zipCode = address?.zipCode,
        city = address?.city,
        active = active
    )

private fun FamilyDto.toDefaultDeliveryTypes() =
    DefaultDeliveryTypes(
        invoice = when (delivery) {
            Delivery.E_INVOICE -> DefaultDeliveryType.ELECTRONICINVOICE
            Delivery.POST -> DefaultDeliveryType.PRINT
            Delivery.EMAIL -> DefaultDeliveryType.EMAIL
        }
    )

private data class CustomerHolder(val customer: Customer)

private data class Customer(
    val customerNumber: String?,
    val name: String,
    val organisationNumber: String,
    val email: String?,
    val emailInvoice: String?,
    val defaultDeliveryTypes: DefaultDeliveryTypes,
    val type: CustomerType = PRIVATE,
    val address1: String?,
    val zipCode: String?,
    val city: String?,
    val active: Boolean
)

private data class DefaultDeliveryTypes(
    val invoice: DefaultDeliveryType
)

@Suppress("SpellCheckingInspection")
private enum class DefaultDeliveryType {
    PRINT,
    EMAIL,
    ELECTRONICINVOICE
}

@Suppress("unused")
private enum class CustomerType {
    PRIVATE,
    COMPANY
}