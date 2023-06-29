import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.1"
	id("io.spring.dependency-management") version "1.1.0"
	id("org.cyclonedx.bom") version "1.7.0"

	kotlin("plugin.serialization") version "1.7.10"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
}

group = "com.backend"
version = "0.0.1-SNAPSHOT"

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

	// Kotlin specific libraries
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

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