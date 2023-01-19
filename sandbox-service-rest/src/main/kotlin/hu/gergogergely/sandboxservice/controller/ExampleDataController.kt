package hu.gergogergely.sandboxservice.controller

import hu.gergogergely.sandboxservice.controller.helper.toResponseEntity
import hu.gergogergely.sandboxservice.exception.CustomException
import hu.gergogergely.sandboxservice.exception.CustomExceptionCode
import hu.gergogergely.sandboxservice.mapper.ExampleDataInterfaceMapper
import hu.gergogergely.sandboxservice.openapi.sandboxserviceexampledata.api.ExampleDataApi
import hu.gergogergely.sandboxservice.openapi.sandboxserviceexampledata.model.ExampleData
import hu.gergogergely.sandboxservice.openapi.sandboxserviceexampledata.model.ExampleDataStatus
import hu.gergogergely.sandboxservice.service.ExampleDataService
import kotlinx.coroutines.flow.Flow
import org.mapstruct.factory.Mappers
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.OffsetDateTime

const val BASE_PATH = "/sandboxService"

@RestController
@RequestMapping(BASE_PATH)
class ExampleDataController(
    private val exampleDataService: ExampleDataService
) : ExampleDataApi {

    private val exampleDataInterfaceMapper = Mappers.getMapper(ExampleDataInterfaceMapper::class.java)

    override fun getExampleData(
        xCorrelationId: String
    ): ResponseEntity<Flow<ExampleData>> {
        return exampleDataService.getExampleData().toResponseEntity(exampleDataInterfaceMapper::toDTO)
    }

    override suspend fun getExampleDataById(xCorrelationId: String, id: String): ResponseEntity<ExampleData> {
        return exampleDataService.getExampleData(id).toResponseEntity(exampleDataInterfaceMapper::toDTO)
    }

    override suspend fun createExampleData(
        xCorrelationId: String,
        exampleData: ExampleData
    ): ResponseEntity<ExampleData> {
        val exampleDataDAO = exampleDataInterfaceMapper.toDAO(exampleData.copy(lastUpdatedTime = OffsetDateTime.now()))
        return exampleDataService.createExampleData(exampleDataDAO).toResponseEntity(exampleDataInterfaceMapper::toDTO)
    }

    override suspend fun upsertExampleDataById(
        xCorrelationId: String,
        id: String,
        exampleData: ExampleData
    ): ResponseEntity<ExampleData> {
        if (id != exampleData.id) {
            throw CustomException(
                CustomExceptionCode.INCONSISTENT_REQUEST,
                "The identifier on the path is not equal to the id provided in the request body"
            )
        }
        val exampleDataDAO = exampleDataInterfaceMapper.toDAO(exampleData.copy(lastUpdatedTime = OffsetDateTime.now()))
        return exampleDataService.upsertExampleData(exampleDataDAO).toResponseEntity(exampleDataInterfaceMapper::toDTO)
    }

    override suspend fun modifyExampleDataById(
        xCorrelationId: String,
        id: String,
        status: ExampleDataStatus
    ): ResponseEntity<ExampleData> {
        return exampleDataService.modifyExampleData(id, status.toString()).toResponseEntity(exampleDataInterfaceMapper::toDTO)
    }

    override suspend fun deleteExampleDataById(
        xCorrelationId: String,
        id: String
    ): ResponseEntity<ExampleData> {
        return exampleDataService.deleteExampleData(id).toResponseEntity(exampleDataInterfaceMapper::toDTO)
    }

}
