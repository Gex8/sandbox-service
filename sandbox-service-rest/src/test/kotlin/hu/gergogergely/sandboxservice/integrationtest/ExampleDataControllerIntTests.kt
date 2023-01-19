package hu.gergogergely.sandboxservice.integrationtest

import hu.gergogergely.sandboxservice.controller.BASE_PATH
import hu.gergogergely.sandboxservice.filter.HEADER_CORRELATION_ID_FIELD
import hu.gergogergely.sandboxservice.openapi.sandboxserviceexampledata.model.ExampleData as ExampleDataDTO
import hu.gergogergely.sandboxservice.model.dao.ExampleData as ExampleDataDAO
import hu.gergogergely.sandboxservice.repository.ExampleDataRepository
import hu.gergogergely.sandboxservice.utility.AbstractTestContainers
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders.ACCEPT
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.test.StepVerifier

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class ExampleDataControllerIntTests : AbstractTestContainers() {

    @Autowired
    private lateinit var webClient: WebTestClient

    @Autowired
    private lateinit var exampleDataRepository: ExampleDataRepository

    @BeforeEach
    fun init() {
        runBlocking {
            exampleDataRepository.deleteAll()
        }
    }

    @Test
    fun `GET request an existing entity and return the found data`() {
        val publicIdentifier = "publicIdentifier01"
        runBlocking {
            exampleDataRepository.save(ExampleDataDAO(publicIdentifier = publicIdentifier))
        }

        webClient.get()
            .uri("$BASE_PATH/exampleData/$publicIdentifier")
            .header(HEADER_CORRELATION_ID_FIELD, "test-correlation-id")
            .exchange()
            .expectStatus().isOk
            .returnResult(ExampleDataDTO::class.java)
            .also { response ->
                StepVerifier.create(response.responseBody)
                    .consumeNextWith { exampleData ->
                        Assertions.assertEquals(publicIdentifier, exampleData.id)
                    }
            }

    }

}