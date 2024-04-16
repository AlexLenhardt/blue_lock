import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.0.0"
	id("io.spring.dependency-management") version "1.0.12.RELEASE"
	id("org.cyclonedx.bom") version "1.7.0"

	kotlin("plugin.serialization") version "1.7.10"
	kotlin("jvm") version "1.7.10"
	kotlin("plugin.spring") version "1.7.10"
}
group = "com.backend"
version = "0.0.2-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	// Standard spring boot libraries
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

	// Kotlin specific libraries
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

	val exposedVersion = "0.40.1"
	implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
	implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
	implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
	implementation("org.jetbrains.exposed:exposed-spring-boot-starter:$exposedVersion")
	implementation("org.postgresql:postgresql")

	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.3")
	developmentOnly("org.springframework.boot:spring-boot-starter-logging")
	developmentOnly("org.springframework.boot:spring-boot-starter-web")

	// Project specific libraries
	implementation("com.auth0:java-jwt:4.3.0")
	implementation("com.github.f4b6a3:uuid-creator:5.2.0")

	// Database dependencies
	runtimeOnly("org.postgresql:postgresql")

	// Development only libraries
	developmentOnly("org.springframework.boot:spring-boot-devtools")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

// Plugin page: https://github.com/CycloneDX/cyclonedx-gradle-plugin
// Plugin license: Apache 2.0 (https://github.com/CycloneDX/cyclonedx-gradle-plugin/blob/master/LICENSE)
tasks.cyclonedxBom {
	setIncludeConfigs(listOf("runtimeClasspath"))
	setSkipConfigs(listOf("compileClasspath", "testCompileClasspath"))
	setProjectType("application")
	setSchemaVersion("1.4")
	setDestination(project.file("build/reports"))
	setOutputName("bom")
	setOutputFormat("all")
}