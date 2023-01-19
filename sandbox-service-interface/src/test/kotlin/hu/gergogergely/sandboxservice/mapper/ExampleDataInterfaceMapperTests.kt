package hu.gergogergely.sandboxservice.mapper

import hu.gergogergely.sandboxservice.mapper.helper.DateTimeMapper
import hu.gergogergely.sandboxservice.model.dao.ExampleData as ExampleDataDAO
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.mapstruct.factory.Mappers
import java.time.Instant

internal class ExampleDataInterfaceMapperTests {

    private val exampleDataInterfaceMapper = Mappers.getMapper(ExampleDataInterfaceMapper::class.java)

    @Test
    fun `should map DAO object to DTO`() {
        val exampleDataDAO = ExampleDataDAO(
            id = "dbIdentifier",
            publicIdentifier = "publicIdentifier",
            updatedAt = Instant.now(),
            status = ExampleDataDAO.Status.ACTIVE,
            connectedData = listOf("a", "b"),
        )

        val exampleDataDTO = exampleDataInterfaceMapper.toDTO(exampleDataDAO)

        assertEquals(exampleDataDAO.publicIdentifier, exampleDataDTO.id)
        assertEquals(exampleDataDAO.updatedAt, DateTimeMapper().toInstant(exampleDataDTO.lastUpdatedTime))
        assertEquals(exampleDataDAO.status.toString(), exampleDataDTO.status.toString())
        assertEquals(exampleDataDAO.connectedData, exampleDataDTO.connectedData)
    }

}