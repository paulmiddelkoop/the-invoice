import org.gradle.api.JavaVersion.VERSION_17
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.run.BootRun

val jacocoVersion = "0.8.8"
extra["netty.version"] = "4.1.74.Final"

plugins {
	id("org.springframework.boot") version "2.6.6"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("org.flywaydb.flyway") version "8.5.7"
	kotlin("jvm") version "1.6.10"
	kotlin("plugin.spring") version "1.6.20"
	jacoco
}
apply(plugin = "io.spring.dependency-management")

group = "se.pamisoft"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive")
	implementation("org.postgresql:postgresql")
	implementation("org.flywaydb:flyway-core")
	implementation("dev.personnummer:personnummer:3.3.3")
	implementation("io.github.microutils:kotlin-logging:2.1.21")
	implementation("io.netty:netty-resolver-dns-native-macos:${properties["netty.version"]}:osx-aarch_64")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.mockito.kotlin:mockito-kotlin:${dependencyManagement.importedProperties["mockito.version"]}")
	testImplementation("com.squareup.okhttp3:mockwebserver:4.9.3")
	testImplementation("com.squareup.okhttp3:okhttp:4.9.3")
	jacocoAnt("org.jacoco:org.jacoco.ant:$jacocoVersion")
	jacocoAgent("org.jacoco:org.jacoco.agent:$jacocoVersion")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.getByName<BootRun>("bootRun") {
	systemProperty("spring.profiles.active", "dev")
}

tasks.test {
	useJUnitPlatform()
	finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
	reports {
		xml.required.set(true)
		html.required.set(false)
	}
}

flyway {
	url = "jdbc:postgresql://localhost:5432/the_invoice"
	user = "the_invoice"
	password = "the_invoice"
}