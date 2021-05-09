package plex.hook.api.services

import io.micronaut.context.annotation.Context
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import plex.hook.api.models.PlexWebhook
import javax.inject.Singleton

@Context
@Singleton
class HookProcessor(
    private val hookValidationService: HookValidationService,
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun processHook(hook: PlexWebhook) {
        logger.info("[Hook Received] $hook")

        if (!hookValidationService.shouldIngestHook(hook)) return


    }

}
