package se.pamisoft.theinvoice.development

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import se.pamisoft.theinvoice.FamilyRepository

@Component
@Profile("dev")
class DataLoader(private val familyRepository: FamilyRepository): ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        familyRepository.upsert(family())
    }
}