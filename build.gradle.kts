plugins {
	java
}

subprojects {
	apply(plugin = "java")
	apply(plugin = "idea")

	group = "hu.gergogergely.sandboxservice"
	version = project.findProperty("serviceVersion")!!

	java.sourceCompatibility = JavaVersion.VERSION_17
	java.targetCompatibility = JavaVersion.VERSION_17


	repositories {
		mavenCentral()
	}

	val developmentOnly = configurations.create("development-Only")
	configurations.runtimeClasspath.configure { extendsFrom(developmentOnly) }
	configurations.compileOnly.configure { extendsFrom(configurations.annotationProcessor.get()) }

	dependencies {
		implementation("org.jetbrains.kotlin:kotlin-reflect")
		implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
		implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
		implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

		developmentOnly("org.springframework.boot:spring-boot-devtools")

		testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
		testImplementation("org.springframework.boot:spring-boot-starter-test")
		testImplementation("org.testcontainers:testcontainers:1.17.3")
		testImplementation("org.testcontainers:junit-jupiter:1.17.3")
	}

	tasks.withType<Wrapper> {
		gradleVersion = "7.6"
	}

	apply {
		from("../gradle/scripts/test-logging.gradle.kts")
	}

	tasks.test {
		useJUnitPlatform()
	}
}
