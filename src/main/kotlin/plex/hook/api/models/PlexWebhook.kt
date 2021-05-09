package plex.hook.api.models

import com.fasterxml.jackson.annotation.JsonProperty
import plex.hook.api.domains.HookDocument
import plex.hook.api.domains.PlexEvent
import java.time.Clock
import java.time.Instant

data class PlexWebhook(
    val event: PlexEvent,
    @JsonProperty("Account") val account: PlexAccount,
    @JsonProperty("Server") val server: PlexServer,
    @JsonProperty("Metadata") val metadata: PlexMetadata?
) {

    fun toHookDocument(): HookDocument {
        val (movieName, showName, episodeName) = if (this.metadata?.type == "movie") {
            Triple(this.metadata.title, null, null)
        } else {
            Triple(null, this.metadata?.grandparentTitle, this.metadata?.title)
        }

        return HookDocument(
            eventType = this.event.name,
            serverName = this.server.title,
            serverId = this.server.uuid,
            userName = this.account.title,
            userId = this.account.id.toString(),
            year = this.metadata?.year?.toShort(),
            movieName = movieName,
            showName = showName,
            episodeName = episodeName,
            timestamp = Instant.now(Clock.systemUTC())
        )
    }
}

data class PlexAccount(
    val id: Long,
    val title: String,
)

data class PlexServer(
    val title: String,
    val uuid: String,
)

data class PlexMetadata(
    val title: String,
    val type: String,
    val year: Int?,
    @JsonProperty("grandparentTitle") val grandparentTitle: String?,
)
