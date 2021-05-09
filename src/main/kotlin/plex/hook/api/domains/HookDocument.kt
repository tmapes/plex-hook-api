package plex.hook.api.domains

import java.time.Instant


data class HookDocument(
    val eventType: String,
    val timestamp: Instant,
    val serverName: String,
    val serverId: String,
    val userName: String,
    val userId: String,
    val year: Short? = null,
    val movieName: String? = null,
    val showName: String? = null,
    val episodeName: String? = null,
)
