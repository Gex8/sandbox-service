package hu.gergogergely.sandboxservice.repository

import hu.gergogergely.sandboxservice.model.dao.ExampleData
import kotlinx.coroutines.flow.Flow

interface ExampleDataRepository {
    fun findAll(): Flow<ExampleData>
    suspend fun findByPublicIdentifier(publicIdentifier: String): ExampleData?
    suspend fun save(exampleData: ExampleData): ExampleData?
    suspend fun delete(exampleData: ExampleData): Long?
    suspend fun deleteAll(): Long?
}
