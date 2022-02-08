package se.pamisoft.theinvoice

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class TestConfig {
    @Bean
    fun flywayMigrationStrategy() =
        FlywayMigrationStrategy { flyway ->
            flyway.clean()
            flyway.migrate()
        }
}