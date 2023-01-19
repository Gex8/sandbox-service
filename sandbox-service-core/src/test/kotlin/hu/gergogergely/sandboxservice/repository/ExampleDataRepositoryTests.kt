package hu.gergogergely.sandboxservice.repository

import hu.gergogergely.sandboxservice.model.dao.ExampleData
import hu.gergogergely.sandboxservice.utility.AbstractTestContainers
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ExampleDataRepositoryTests : AbstractTestContainers() {

    @SpringBootApplication
    class TestConfiguration

    @Autowired
    private lateinit var repository: ExampleDataRepository

    @BeforeEach
    fun init() {
        runBlocking {
            repository.deleteAll()
        }
    }

    @Test
    fun `should find ExampleData by publicIdentifier`() {
        val publicIdentifier = "publicIdentifier01"
        runBlocking {
            repository.save(ExampleData(publicIdentifier = publicIdentifier))
        }

        val persistedEntity = runBlocking { repository.findByPublicIdentifier(publicIdentifier) }

        Assertions.assertEquals(publicIdentifier, persistedEntity?.publicIdentifier)
    }

}