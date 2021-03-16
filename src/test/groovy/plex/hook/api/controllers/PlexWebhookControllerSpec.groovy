package plex.hook.api.controllers


import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.http.HttpStatus
import io.micronaut.http.exceptions.HttpStatusException
import plex.hook.api.AbstractSpecification
import plex.hook.api.models.PlexWebhook
import plex.hook.api.services.HookProcessor

class PlexWebhookControllerSpec extends AbstractSpecification {

    def objectMapperMock = Mock(ObjectMapper)
    def hookProcessorMock = Mock(HookProcessor)

    def plexWebhookController = new PlexWebhookController(hookProcessorMock, objectMapperMock)

    def 'Successful JSON parse'() {
        given:
        def toProcessWebhook = getPlexWebhook()
        def jsonPayload = """{
            "event": "media.play",
            "Account": {
                "id": 1,
                "title": "plex-user"
            },
            "Server": {
                "title": "plex-server",
                "uuid": "aa11bb22cc33dd44ee55"
            },
            "Metadata": {
                "title": "The Finale",
                "type": "episode",
                "year": 2020,
                "grandparentTitle": "The Show"
            }
        }"""
        def requestBody = [payload: jsonPayload]

        when:
        plexWebhookController.acceptWebhook(requestBody)

        then:
        1 * objectMapperMock.readValue(jsonPayload, PlexWebhook.class) >> toProcessWebhook
        1 * hookProcessorMock.processHook(toProcessWebhook)

        0 * _
    }

    def 'Failure to parse json results in a 400'() {
        given:
        def requestBody = [payload: "value"]

        when:
        plexWebhookController.acceptWebhook(requestBody)

        then:
        1 * objectMapperMock.readValue("value", PlexWebhook.class) >> { throw new JsonMappingException("Failure") }

        def caught = thrown(HttpStatusException)
        HttpStatus.BAD_REQUEST == caught.status
        caught.message

        0 * _
    }
}
