package se.pamisoft.theinvoice.development

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("dev", "test")
class FlywayConfiguration {
    @Bean
    fun flywayMigrationStrategy() =
        FlywayMigrationStrategy { flyway ->
            flyway.clean()
            flyway.migrate()
        }
}