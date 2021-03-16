package plex.hook.api.configs

import io.micronaut.context.annotation.ConfigurationInject
import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.context.annotation.Context
import plex.hook.api.domains.PlexEvent
import javax.inject.Singleton

@Context
@Singleton
@ConfigurationProperties("plex")
data class PlexConfig @ConfigurationInject constructor(
        val enabledEvents: List<PlexEvent>,
        val enabledServers: Set<String>,
        val enabledUsers: Set<String>,
)
