package plex.hook.api.controllers

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.exceptions.HttpStatusException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import plex.hook.api.domains.HookDocument
import plex.hook.api.models.PlexWebhook
import plex.hook.api.services.HookProcessor

@Controller
class PlexWebhookController(
    private val hookProcessor: HookProcessor,
    private val objectMapper: ObjectMapper
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * Plex is mean and does not set the content-type of the `payload` form-data object
     * so we need to manually parse it into our POJO.
     * (Micronaut will auto-parse it to a POJO *if* the sender says it's application/json
     */
    @Post(consumes = [MediaType.MULTIPART_FORM_DATA], single = true)
    @Status(HttpStatus.NO_CONTENT)
    fun acceptWebhook(body: Map<String, String>) {
        val payload = try {
            objectMapper.readValue(body["payload"]!!, PlexWebhook::class.java)
        } catch (e: JsonProcessingException) {
            val message = "Failed to parse $body"
            logger.error(message, e)
            throw HttpStatusException(HttpStatus.BAD_REQUEST, message)
        }
        hookProcessor.processHook(payload)
    }

    @Get(produces = [MediaType.APPLICATION_JSON])
    fun getSavedHooks(
        @QueryValue("event_type") eventType: String?,
        @QueryValue("user_name") userName: String?,
        @QueryValue("page", defaultValue = "0") page: Int,
        @QueryValue("per_page", defaultValue = "100") perPage: Int,
    ): List<HookDocument> {
        return hookProcessor.getSavedHooks(eventType, userName, page, perPage)
    }

}
