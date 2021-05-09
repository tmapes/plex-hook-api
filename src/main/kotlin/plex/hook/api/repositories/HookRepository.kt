package plex.hook.api.repositories

import com.mongodb.client.model.IndexOptions
import io.micronaut.context.annotation.Context
import io.micronaut.context.annotation.Value
import org.bson.*
import org.litote.kmongo.*
import org.slf4j.LoggerFactory
import plex.hook.api.domains.HookDocument
import javax.annotation.PreDestroy
import javax.inject.Singleton

@Context
@Singleton
class HookRepository(
    @Value("\${mongodb.uri}") private val mongoUri: String,
    @Value("\${mongodb.db:plexHooks}") private val databaseName: String,
    @Value("\${mongodb.collection:plexHooks}") private val collectionName: String,
) {
    private final val logger = LoggerFactory.getLogger(this::class.java)

    private final val connection = KMongo.createClient(mongoUri)
    private final val db = connection.getDatabase(databaseName)
    private final val collection = db.getCollection<HookDocument>(collectionName)

    init {
        logger.info("Created Mongo Connection : ${connection.listDatabaseNames()} : ${db.name}")
        logger.info("Initialized with collection $collection")

        collection.ensureIndex(descending(HookDocument::timestamp), IndexOptions().name("timestamp_-1"))
        collection.ensureIndex(descending(HookDocument::eventType), IndexOptions().name("eventType_-1"))
        collection.ensureIndex(descending(HookDocument::userName), IndexOptions().name("userName_-1"))
    }

    @PreDestroy
    private fun destroyConnection() {
        logger.info("Closing Mongo Connection")
        connection.close()
    }

    fun saveWebhook(document: HookDocument) {
        return collection.save(document)
    }

    fun getSavedHooks(eventType: String?, userName: String?, limit: Int = 100, skipCount: Int = 0): List<HookDocument> {
        val filter = BsonDocument()

        eventType?.let { filter.put("eventType", BsonString(it)) }
        userName?.let { filter.put("userName", BsonString(it)) }

        return collection
            .find(filter)
            .limit(limit)
            .skip(skipCount)
            .sort(descending(HookDocument::timestamp))
            .toList()
    }

}
