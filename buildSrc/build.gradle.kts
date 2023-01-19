plugins {
    `kotlin-dsl`
}

repositories {
    maven {
        url = uri("https://plugins.gradle.org/m2/")
    }
}

dependencies {
    // openapi generation
    implementation("de.undercouch.download:de.undercouch.download.gradle.plugin:4.1.1")
    implementation("org.openapi.generator:org.openapi.generator.gradle.plugin:6.2.1")
}
