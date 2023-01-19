plugins {
    id("java")
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    `openapi-generator`
    kotlin("kapt")
}

dependencies {
    implementation(project(":sandbox-service-core"))

    // openapi generation
    implementation("org.openapitools:jackson-databind-nullable:0.2.3")
    implementation("com.google.code.findbugs:jsr305:3.0.2")
    implementation("org.springdoc:springdoc-openapi-webflux-ui:1.6.11")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("io.swagger:swagger-annotations:1.6.6")

    val mapstructVersion = project.findProperty("mapstructVersion")!!
    kapt("org.mapstruct:mapstruct-processor:$mapstructVersion")
    implementation("org.mapstruct:mapstruct:$mapstructVersion")
}

tasks["bootJar"].enabled = false
tasks["jar"].enabled = true

val sourceJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

val openApiGeneratedPath = "$buildDir${File.separator}generated${File.separator}openapi"
sourceSets.main { java.srcDirs("$openApiGeneratedPath/src/main/kotlin") }
sourceSets.main { java.srcDirs("$openApiGeneratedPath/src/main/java") }
tasks.compileKotlin { dependsOn(tasks.named("downloadAndGenerateOpenapiClientsAndServerInterfaces")) }
