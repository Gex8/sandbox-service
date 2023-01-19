import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("application")
	id("org.springframework.boot")
	id("io.spring.dependency-management")
	kotlin("jvm")
	kotlin("plugin.spring")
}

application { mainClass.set("hu.gergogergely.sandboxservice.SandboxServiceRestApplicationKt") }
tasks.jar {
	manifest {
		attributes["Main-Class"] = "hu.gergogergely.sandboxservice.SandboxServiceRestApplicationKt"
	}
}

dependencies {
	implementation(project(":sandbox-service-core"))
	implementation(project(":sandbox-service-interface"))

	implementation("org.springframework.boot:spring-boot-starter-web")

	val mapstructVersion = project.findProperty("mapstructVersion")!!
	implementation("org.mapstruct:mapstruct:$mapstructVersion")

	testImplementation("org.springframework.boot:spring-boot-starter-webflux")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("io.mockk:mockk:1.13.2")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict", "-Xsanitize-parentheses")
		jvmTarget = "17"
	}
}
