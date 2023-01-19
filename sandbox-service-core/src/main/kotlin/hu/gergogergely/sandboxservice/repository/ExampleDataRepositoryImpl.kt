package hu.gergogergely.sandboxservice.repository

import hu.gergogergely.sandboxservice.configuration.EXAMPLE_DATA_COLLECTION
import hu.gergogergely.sandboxservice.configuration.PUBLIC_IDENTIFIER_INDEX
import hu.gergogergely.sandboxservice.model.dao.ExampleData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class ExampleDataRepositoryImpl(
    private val mongoTemplate: ReactiveMongoOperations
) : ExampleDataRepository {

    override fun findAll(): Flow<ExampleData> =
        mongoTemplate.findAll(ExampleData::class.java, EXAMPLE_DATA_COLLECTION).asFlow()

    override suspend fun findByPublicIdentifier(publicIdentifier: String): ExampleData? =
        mongoTemplate.findOne(
            Query(Criteria.where(PUBLIC_IDENTIFIER_INDEX).`is`(publicIdentifier)),
            ExampleData::class.java,
            EXAMPLE_DATA_COLLECTION
        ).awaitSingleOrNull()

    override suspend fun save(exampleData: ExampleData): ExampleData? =
        mongoTemplate.save(exampleData).awaitSingleOrNull()

    override suspend fun delete(exampleData: ExampleData): Long? =
        mongoTemplate.remove(exampleData).awaitSingleOrNull()?.deletedCount

    override suspend fun deleteAll(): Long? =
        mongoTemplate.remove(Query(), EXAMPLE_DATA_COLLECTION).awaitSingleOrNull()?.deletedCount

}
