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
    private val hookRepository: HookRepository
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun processHook(hook: PlexWebhook) {
        logger.info("[Hook Received] $hook")

        if (!hookValidationService.shouldIngestHook(hook)) return

        hook.toHookDocument().let {
            hookRepository.saveWebhook(it)
            logger.info("Saved $it")
        }

    }

    fun getSavedHooks(eventType: String?, userName: String?, page: Int, perPage: Int): List<HookDocument> {
        logger.info("[Lookup Request] eventType '$eventType' userName '$userName' page '$page' perPage '$perPage'")
        return hookRepository.getSavedHooks(eventType, userName, limit = perPage, skipCount = page * perPage)
    }

}
