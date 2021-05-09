package plex.hook.api.services

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import plex.hook.api.configs.PlexConfig
import plex.hook.api.models.PlexWebhook
import javax.inject.Singleton

@Singleton
class HookValidationService(
    private val plexConfig: PlexConfig,
    private val metricsService: MetricsService,
) {

    private final val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun shouldIngestHook(hook: PlexWebhook): Boolean {
        metricsService.incReceived()

        if (hook.event !in plexConfig.enabledEvents) {
            logger.info("[ IGNORING || Unwanted Event] ${hook.event} :: $hook")
            metricsService.incUnwantedEvent()
            return false
        } else if (hook.server.title !in plexConfig.enabledServers) {
            logger.info("[ IGNORING || Unwanted Server] ${hook.server.title} :: $hook")
            metricsService.incUnwantedServer()
            return false
        } else if (plexConfig.enableAllUsers.not() && hook.account.title !in plexConfig.enabledUsers) {
            logger.info("[ IGNORING || Unwanted User] ${hook.account.title} :: $hook")
            metricsService.incUnwantedUser()
            return false
        }

        return true
    }
}
