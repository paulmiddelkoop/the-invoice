package se.pamisoft.theinvoice

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.stereotype.Repository
import org.springframework.test.context.ActiveProfiles
import se.pamisoft.theinvoice.development.DevelopmentConfiguration

@JdbcTest(includeFilters = [ComponentScan.Filter(Repository::class)])
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("test")
@Import(DevelopmentConfiguration::class)
annotation class RepositoryTest