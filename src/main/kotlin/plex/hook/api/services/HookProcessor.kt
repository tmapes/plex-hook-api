package plex.hook.api.services

import io.micronaut.context.annotation.Context
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import plex.hook.api.domains.HookDocument
import plex.hook.api.models.PlexWebhook
import plex.hook.api.repositories.HookRepository
import javax.inject.Singleton

@Context
@Singleton
class HookProcessor(
    private val hookValidationService: HookValidationService,
    private val hookRepository: HookRepository?
) {
    private final val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private final val mongoDisabled = hookRepository == null

    fun processHook(hook: PlexWebhook) {
        logger.info("[Hook Received] $hook")

        if (!hookValidationService.shouldIngestHook(hook)) return

        if (mongoDisabled) {
            logger.info("Not Saving Hook because Mongo Integration Is Disabled")
            return
        }

        hook.toHookDocument().let {
            hookRepository!!.saveWebhook(it)
            logger.info("Saved $it")
        }

    }

    fun getSavedHooks(eventType: String?, userName: String?, page: Int, perPage: Int): List<HookDocument> {
        logger.info("[Lookup Request] eventType '$eventType' userName '$userName' page '$page' perPage '$perPage'")

        if (mongoDisabled) return emptyList()

        return hookRepository!!.getSavedHooks(eventType, userName, limit = perPage, skipCount = page * perPage)
    }

}
