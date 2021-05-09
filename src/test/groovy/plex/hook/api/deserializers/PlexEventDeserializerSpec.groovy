package plex.hook.api.deserializers

import com.fasterxml.jackson.databind.ObjectMapper
import plex.hook.api.AbstractSpecification
import plex.hook.api.domains.PlexEvent
import spock.lang.Shared
import spock.lang.Unroll

class PlexEventDeserializerSpec extends AbstractSpecification {

    @Shared
    def objectMapper = new ObjectMapper()

    @Unroll
    def "#scenario results in #expectedOutput"() {
        expect:
        def output = objectMapper.readValue(scenario, PlexEvent.class)
        expectedOutput == output

        where:
        scenario             || expectedOutput
        "\"media.play\""     || PlexEvent.PLAY
        "\"media.pause\""    || PlexEvent.PAUSE
        "\"media.resume\""   || PlexEvent.RESUME
        "\"media.stop\""     || PlexEvent.STOP
        "\"library.new\""    || PlexEvent.LIBRARY_NEW_ITEM
        "\"server.deleted\"" || PlexEvent.UNKNOWN
    }
}
