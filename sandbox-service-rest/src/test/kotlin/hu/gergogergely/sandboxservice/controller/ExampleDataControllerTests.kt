package hu.gergogergely.sandboxservice.controller

import hu.gergogergely.sandboxservice.model.dao.ExampleData
import hu.gergogergely.sandboxservice.service.ExampleDataService
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.http.HttpStatus

internal class ExampleDataControllerTests {

    private val exampleDataService = mockk<ExampleDataService>()

    private val exampleDataController = ExampleDataController(exampleDataService)

    @Test
    fun `getExampleData should respond OK and return entities`() {
        every { exampleDataService.getExampleData() } returns flowOf(
            ExampleData(publicIdentifier = "id01"),
            ExampleData(publicIdentifier = "id02")
        )

        runBlocking {
            val response = exampleDataController.getExampleData("corrId")
            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(2, response.body?.toList()?.size)
        }
    }

    // TODO: write more tests...

}