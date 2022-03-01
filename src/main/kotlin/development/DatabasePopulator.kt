package se.pamisoft.theinvoice.development

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import se.pamisoft.theinvoice.family.Delivery.E_INVOICE
import se.pamisoft.theinvoice.family.Family
import se.pamisoft.theinvoice.family.FamilyRepository
import se.pamisoft.theinvoice.family.Guardian
import se.pamisoft.theinvoice.family.PersonalIdentityNumber
import se.pamisoft.theinvoice.family.income.Income
import se.pamisoft.theinvoice.family.income.IncomeRepository
import java.time.LocalDate.now

@Component
@Profile("dev")
class DatabasePopulator(
    private val familyRepository: FamilyRepository,
    private val incomeRepository: IncomeRepository
) :
    ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        val family = Family(
            guardian1 = Guardian(firstName = "John", lastName = "Doe"),
            guardian2 = Guardian(firstName = "Jane", lastName = "Doe"),
            personalIdentityNumber = PersonalIdentityNumber("19890201-3286"),
            delivery = E_INVOICE,
            email = "john@gmail.com",
            customerNumber = "80"
        ).apply { familyRepository.upsert(this) }
        incomeRepository.upsert(Income(max = true, amount = null, startsOn = now()), family.id)
        incomeRepository.upsert(Income(max = false, amount = 40_000, startsOn = now().minusYears(1)), family.id)

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