package plex.hook.api.deserializers

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import plex.hook.api.domains.PlexEvent
import javax.inject.Singleton

@Singleton
class PlexEventDeserializer : StdDeserializer<PlexEvent>(PlexEvent::class.java) {

    override fun deserialize(jsonParser: JsonParser, ctxt: DeserializationContext?): PlexEvent {
        val node: JsonNode = jsonParser.codec.readTree(jsonParser)
        val value = node.textValue()
        return PlexEvent.from(value)
    }

}
