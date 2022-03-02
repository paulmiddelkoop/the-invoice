package se.pamisoft.theinvoice

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import se.pamisoft.theinvoice.family.FamilyController

@SpringBootTest
@ActiveProfiles("test")
class ApplicationTestIT(@Autowired private val familyController: FamilyController) {
	@Test
	fun `Should load context`() {
		assertThat(familyController).isNotNull
	}
}

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@ActiveProfiles("dev")
class ApplicationDevelopmentIT(@Autowired private val familyController: FamilyController) {

	@Test
	fun `Should load context`() {
		assertThat(familyController).isNotNull
	}
}