package plex.hook.api.models

import com.fasterxml.jackson.annotation.JsonProperty
import plex.hook.api.domains.PlexEvent

data class PlexWebhook(
        val event: PlexEvent,
        @JsonProperty("Account") val account: PlexAccount,
        @JsonProperty("Server") val server: PlexServer,
        @JsonProperty("Metadata") val metadata: PlexMetadata
)

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
        val year: Int,
        @JsonProperty("grandparentTitle") val grandparentTitle: String?,
)