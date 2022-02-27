package se.pamisoft.theinvoice.development

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import se.pamisoft.theinvoice.*
import se.pamisoft.theinvoice.Delivery.E_INVOICE
import java.time.LocalDate

@Component
@Profile("dev")
class DatabasePopulator(private val familyRepository: FamilyRepository) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        familyRepository.upsert(
            Family(
                guardian1 = Guardian(firstName = "John", lastName = "Doe"),
                guardian2 = Guardian(firstName = "Jane", lastName = "Doe"),
                personalIdentityNumber = PersonalIdentityNumber("19890201-3286"),
                delivery = E_INVOICE,
                email = "john@gmail.com",
                customerNumber = "80",
                incomes = listOf(
                    Income(max = true, amount = null, changedOn = LocalDate.now()),
                    Income(max = false, amount = 40_000, changedOn = LocalDate.now().minusMonths(10))
                )
            )
        )
        familyRepository.upsert(
            Family(
                guardian1 = Guardian(firstName = "Paul", lastName = "Middelkoop"),
                guardian2 = null,
                personalIdentityNumber = PersonalIdentityNumber("19890201-3286"),
                delivery = E_INVOICE,
                email = "paul@gmail.com",
                customerNumber = "81"
            )
        )

    }
}