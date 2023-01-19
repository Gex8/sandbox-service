package hu.gergogergely.sandboxservice.service

import hu.gergogergely.sandboxservice.exception.CustomException
import hu.gergogergely.sandboxservice.exception.CustomExceptionCode
import hu.gergogergely.sandboxservice.model.dao.ExampleData
import hu.gergogergely.sandboxservice.repository.ExampleDataRepository
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service

@Service
class ExampleDataService(
    private val exampleDataRepository: ExampleDataRepository
) {

    fun getExampleData(): Flow<ExampleData> {
        return exampleDataRepository.findAll()
    }

    suspend fun getExampleData(publicIdentifier: String): ExampleData? {
        return exampleDataRepository.findByPublicIdentifier(publicIdentifier)
    }

    suspend fun createExampleData(exampleData: ExampleData): ExampleData? {
        if (getExampleData(exampleData.publicIdentifier) != null) {
            throw CustomException(
                CustomExceptionCode.ALREADY_EXISTS,
                "An entity with the given identifier is already exists."
            )
        }

        return exampleDataRepository.save(exampleData)
    }

    suspend fun upsertExampleData(exampleData: ExampleData): ExampleData? {
        val savedEntity = getExampleData(exampleData.publicIdentifier)

        return exampleDataRepository.save(exampleData.copy(id = savedEntity?.id))
    }

    suspend fun modifyExampleData(publicIdentifier: String, status: String): ExampleData? {
        val savedEntity = getExampleData(publicIdentifier)

        return if (savedEntity != null) {
            exampleDataRepository.save(savedEntity.copy(status = ExampleData.Status.valueOf(status)))
        } else {
            null
        }
    }

    suspend fun deleteExampleData(publicIdentifier: String): ExampleData? {
        val savedEntity = getExampleData(publicIdentifier)
        savedEntity?.let { exampleDataRepository.delete(it) }
        return savedEntity
    }

}
