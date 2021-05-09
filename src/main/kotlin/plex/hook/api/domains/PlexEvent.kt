package plex.hook.api.domains

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import plex.hook.api.deserializers.PlexEventDeserializer

@JsonDeserialize(using = PlexEventDeserializer::class)
enum class PlexEvent(val value: String) {
    PLAY("media.play"),
    STOP("media.stop"),
    PAUSE("media.pause"),
    RESUME("media.resume"),
    LIBRARY_NEW_ITEM("library.new"),
    UNKNOWN("unknown");

    companion object {
        private val mapValues = values().associateBy { it.value }

        @JvmStatic
        fun from(input: String?): PlexEvent {
            return mapValues[input] ?: UNKNOWN
        }
    }
}
