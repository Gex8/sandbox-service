package hu.gergogergely.sandboxservice.configuration

import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.index.Index
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

const val EXAMPLE_DATA_COLLECTION = "exampleData"
const val DEFAULT_INDEX = "_id"
const val PUBLIC_IDENTIFIER_INDEX = "publicIdentifier"

@Configuration
class MongoConfiguration

@EnableReactiveMongoRepositories(basePackages = ["hu.gergogergely.sandboxservice.repository"])
class MongoEnable

@Configuration
@DependsOn("reactiveMongoTemplate")
class CollectionsConfig(val mongoTemplate: ReactiveMongoTemplate) {
    @PostConstruct
    fun initIndexes() {
        fun ensureIndex(collection: String, index: String, sortDirection: Sort.Direction = Sort.DEFAULT_DIRECTION) {
            mongoTemplate.indexOps(collection).ensureIndex(Index(index, sortDirection).named(index)).subscribe()
        }

        ensureIndex(EXAMPLE_DATA_COLLECTION, DEFAULT_INDEX)
        ensureIndex(EXAMPLE_DATA_COLLECTION, PUBLIC_IDENTIFIER_INDEX)
    }
}
