package se.pamisoft.theinvoice

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.stereotype.Repository
import org.springframework.test.context.ActiveProfiles

@JdbcTest
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("test")
@ComponentScan(includeFilters = [ComponentScan.Filter(Repository::class)])
annotation class RepositoryTest