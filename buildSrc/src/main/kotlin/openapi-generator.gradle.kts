import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

tasks.create("downloadAndGenerateOpenapiClientsAndServerInterfaces")

class OpenapiClientConfig(
    val name: String,
    val basePackage: String,
    val version: String,
    val clientGenerator: String,
    val url: String,
    val compressedPath: String? = null
)

class OpenapiServerConfig(
    val name: String,
    val language: String
)

fun getOpenapiGeneratorConfig(): Pair<List<OpenapiClientConfig>, List<OpenapiServerConfig>> {
    val openapiGeneratorConfigJsonFile = File("${projectDir}${File.separator}openapi-generator.config.json")
    if(openapiGeneratorConfigJsonFile.exists() == false) {
        return Pair(emptyList(),emptyList())
    }

    val openapiGeneratorConfig =
        groovy.json.JsonSlurper().parseText(openapiGeneratorConfigJsonFile.readText()) as Map<String, List<Any>>
    val openapiClientConfigs = (openapiGeneratorConfig["client"] as List<Map<String, Any>>? ?: emptyList())
        .map { openapiClientConfig ->
            val version = openapiClientConfig["version"] as String
            OpenapiClientConfig(
                name = openapiClientConfig["name"] as String,
                basePackage = openapiClientConfig["basePackage"] as String,
                version = openapiClientConfig["version"] as String,
                clientGenerator = openapiClientConfig["clientGenerator"] as String,
                url = (openapiClientConfig["maskedUrl"] as String).replace("**VERSION**", version),
                compressedPath = openapiClientConfig["compressedPath"] as String?,
            )
        }
    val openapiServerConfigs = (openapiGeneratorConfig["server"] as List<Map<String, Any>>? ?: emptyList())
        .map { openapiServerConfig ->
            OpenapiServerConfig(
                name = openapiServerConfig["name"] as String,
                language = openapiServerConfig["language"] as String
            )
        }
    return Pair(openapiClientConfigs, openapiServerConfigs)
}

val openapiClientConfigList: List<OpenapiClientConfig> = getOpenapiGeneratorConfig().first
val openapiServerConfigList: List<OpenapiServerConfig> = getOpenapiGeneratorConfig().second

fun getZippedFileName(url: String, withoutFileExtension: Boolean): String = url.split("/").last().let { zippedFile ->
    return if(withoutFileExtension) {
        zippedFile.substringBeforeLast(".")
    } else {
        zippedFile
    }
}

val openApiGeneratedPath = "$buildDir${File.separator}generated${File.separator}openapi"
//sourceSets.main { java.srcDirs("$openApiGeneratedPath/src/main/kotlin") }
//sourceSets.main { java.srcDirs("$openApiGeneratedPath/src/main/java") }

val downloadClientFiles = tasks.create("downloadClientFiles")
val handleZippedClientFiles = tasks.create("handleZippedClientFiles")
val generateOpenapiClients = tasks.create("generateOpenapiClients")
var copyGeneratorIgnore = tasks.register<Copy>("ignore") {
    from(file("$rootDir/.openapi-generator-ignore"))
    into(file(openApiGeneratedPath))
}

handleZippedClientFiles.dependsOn(downloadClientFiles)
generateOpenapiClients.dependsOn(downloadClientFiles, handleZippedClientFiles)
tasks.named("downloadAndGenerateOpenapiClientsAndServerInterfaces") { dependsOn(generateOpenapiClients) }

val filesToDownload = openapiClientConfigList.filter{ it.version != "local" }.mapNotNull { openapiClientConfig ->
    openapiClientConfig.url
}

if (filesToDownload.isNotEmpty()) {
    val downloadTaskName = "download_clients"
    tasks.register<de.undercouch.gradle.tasks.download.Download>(downloadTaskName) {
        src(filesToDownload)
        dest(openApiGeneratedPath)
        overwrite(true)
    }
    downloadClientFiles.dependsOn(downloadTaskName)
}

openapiClientConfigList
    .filter { it.compressedPath != null }
    .map { openapiClientConfig ->
        val zippedFile = getZippedFileName(openapiClientConfig.url, false)
        val unzipTask = tasks.create<Copy>("unpack_${openapiClientConfig.name}") {
            from(zipTree("$openApiGeneratedPath${File.separator}$zippedFile"))
            into("$openApiGeneratedPath${File.separator}${getZippedFileName(openapiClientConfig.url, true)}")
        }
        handleZippedClientFiles.dependsOn(unzipTask)
        unzipTask.dependsOn(downloadClientFiles)
    }

openapiClientConfigList.forEach { openapiClientConfig ->
    val clientName = openapiClientConfig.name
    val basePackage = openapiClientConfig.basePackage
    val version = openapiClientConfig.version
    val clientGenerator = openapiClientConfig.clientGenerator
    var filePath = "$openApiGeneratedPath${File.separator}" +
            "${openapiClientConfig.compressedPath?.let { getZippedFileName(openapiClientConfig.url, true) + "${File.separator}$it"} ?: "$clientName.yaml"}"
    if(version == "local") {
        filePath = "$projectDir${File.separator}src${File.separator}main${File.separator}resources${File.separator}openapi${File.separator}${openapiClientConfig.url}"
    }
    // OpenApi client generation
    val ifPackage = "$basePackage.openapi.$clientName"
        .replace(File.separator, ".")
        .replace(".yaml", "")
        .replace("-", "")
    val iftask = tasks.create<GenerateTask>("client_$ifPackage") {
        generatorName.set(clientGenerator)
        inputSpec.set(filePath)
        outputDir.set(openApiGeneratedPath)
        apiPackage.set("$ifPackage.api")
        modelPackage.set("$ifPackage.model")
        invokerPackage.set("$ifPackage.invoker")
        configOptions.set(
            when (clientGenerator) {
                "kotlin" -> {
                    mapOf(
                        "dateLibrary" to "java8",
                        "serializationLibrary" to "jackson",
                        "library" to "jvm-retrofit2",
                        "useCoroutines" to "true",
                        "collectionType" to "array" // TODO: until issue will be fixed: https://github.com/OpenAPITools/openapi-generator/issues/8560
                    )
                }
                "java" -> {
                    mapOf(
                        "java8" to "false",
                        "dateLibrary" to "java8",
                        "serializationLibrary" to "jackson",
                        "library" to "webclient",
                        "useBeanValidation" to "true"
                    )
                }
                else -> {
                    mapOf()
                }
            }
        )
    }

    iftask.dependsOn(downloadClientFiles, handleZippedClientFiles)
    generateOpenapiClients.dependsOn(iftask)
}

fun getIfPackageName(fullPath: String): String =
    "hu.gergogergely.sandboxservice." + fullPath.substring(fullPath.indexOf("openapi"))
        .replace(File.separator, ".")
        .replace(".yaml", "")
        .replace("-", "")

// OpenApi server generation
fileTree("${projectDir}/src/main/resources/openapi").matching {
    exclude("external")
}.forEach { file: File ->
    val language = openapiServerConfigList.singleOrNull { it.name == file.name.substringBeforeLast(".") }?.language ?: "kotlin"
    val fullPath = file.absolutePath
    val ifPackage = getIfPackageName(fullPath)
    val iftask = tasks.create<GenerateTask>("server_" + ifPackage) {
        generatorName.set(if(language == "kotlin") "kotlin-spring" else "spring")
        inputSpec.set(fullPath)
        outputDir.set(openApiGeneratedPath)
        apiPackage.set("$ifPackage.api")
        modelPackage.set("$ifPackage.model")
        configOptions.set(
            when (language) {
                "kotlin" -> {
                    mapOf(
                        "library" to "spring-boot",
                        "reactive" to "true",
                        "interfaceOnly" to "true",
                        "enumPropertyNaming" to "UPPERCASE",
                        "useBeanValidation" to "false"
                    )
                }
                "java" -> {
                    mapOf(
                        "reactive" to "true",
                        "skipDefaultInterface" to "true",
                        "interfaceOnly" to "true",
                        "delegatePattern" to "true"
                    )
                }
                else -> {
                    mapOf()
                }
            }
        )
    }
    tasks.named("downloadAndGenerateOpenapiClientsAndServerInterfaces") { dependsOn(iftask) }
}
