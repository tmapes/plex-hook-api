package plex.hook.api.services

import io.micronaut.context.annotation.Context
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import plex.hook.api.configs.PlexConfig
import plex.hook.api.models.PlexWebhook
import javax.inject.Singleton

@Context
@Singleton
class HookProcessor(
        private val plexConfig: PlexConfig,
        private val metricsService: MetricsService,
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun processHook(hook: PlexWebhook) {
        logger.info("[Processing Hook] $hook")
        metricsService.incReceived()
        if (hook.event !in plexConfig.enabledEvents) {
            logger.info("[Unwanted Event] ${hook.event} :: $hook")
            metricsService.incUnwantedEvent()
            return
        }
        if (hook.server.title !in plexConfig.enabledServers) {
            logger.info("[Unwanted Server] ${hook.server.title} :: $hook")
            metricsService.incUnwantedServer()
            return
        }
        if (hook.account.title !in plexConfig.enabledUsers) {
            logger.info("[Unwanted User] ${hook.account.title} :: $hook")
            metricsService.incUnwantedUser()
            return
        }

    }

}