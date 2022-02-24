package se.pamisoft.theinvoice.development

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class DevelopmentConfiguration {
    @Bean
    @Profile("dev", "test")
    fun flywayMigrationStrategy() =
        FlywayMigrationStrategy { flyway ->
            flyway.clean()
            flyway.migrate()
        }

    @Bean
    @Profile("dev")
    fun corsConfigurer(): WebMvcConfigurer = object : WebMvcConfigurer {
        override fun addCorsMappings(registry: CorsRegistry) {
            registry.addMapping("/api/v1/**").allowedMethods("*")
        }
    }
}